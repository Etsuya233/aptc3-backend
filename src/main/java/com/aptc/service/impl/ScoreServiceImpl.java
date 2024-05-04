package com.aptc.service.impl;

import com.aptc.exception.DataException;
import com.aptc.exception.DataProcessingException;
import com.aptc.exception.FileIOException;
import com.aptc.mapper.*;
import com.aptc.pojo.Score;
import com.aptc.pojo.Song;
import com.aptc.pojo.User;
import com.aptc.pojo.dto.ImportScoreDTO;
import com.aptc.pojo.dto.UserExportSt3VO;
import com.aptc.pojo.dto.UserScoreDTO;
import com.aptc.pojo.dto.UserScoreQueryDTO;
import com.aptc.pojo.vo.UserB30VO;
import com.aptc.pojo.vo.UserPTTVO;
import com.aptc.pojo.vo.UserScoreVO;
import com.aptc.result.PageResult;
import com.aptc.service.ScoreService;
import com.aptc.utils.ArcaeaUtils;
import com.aptc.utils.BaseContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScoreServiceImpl implements ScoreService {
	private final ScoreMapper scoreMapper;
	private final UserMapper userMapper;
	private final SongMapper songMapper;
	private final PttHistoryMapper pttHistoryMapper;

	@Value("${ety.path.temp}")
	private String tempPath;

	public ScoreServiceImpl(ScoreMapper scoreMapper, UserMapper userMapper, SongMapper songMapper, PttHistoryMapper pttHistoryMapper) {
		this.scoreMapper = scoreMapper;
		this.userMapper = userMapper;
		this.songMapper = songMapper;
		this.pttHistoryMapper = pttHistoryMapper;
	}

	@Override
	public PageResult getAllScore(UserScoreQueryDTO userScoreQueryDTO) {
		Integer uid = BaseContext.getCurrentId();
		userScoreQueryDTO.setUid(uid);
		PageHelper.startPage(userScoreQueryDTO.getPageNum(), userScoreQueryDTO.getPageSize());
		List<UserScoreVO> list = scoreMapper.userScorePageQuery(userScoreQueryDTO);
		PageInfo<UserScoreVO> pageInfo = PageInfo.of(list);

		PageResult pageResult = new PageResult();
		pageResult.setRecords(list);
		pageResult.setTotal((int) pageInfo.getTotal());

		return pageResult;
	}

	@Override
	public void updateScore(UserScoreDTO userScoreDTO) {
		userScoreDTO.setUid(BaseContext.getCurrentId());
		if(userScoreDTO.getType() != 5) scoreMapper.updateScore(userScoreDTO);
		else scoreMapper.insertScore(userScoreDTO);
	}

	@Override
	public List<UserB30VO> getB30(Integer pageSize) {
		Integer uid = BaseContext.getCurrentId();
		PageHelper.startPage(1, pageSize);
			List<UserB30VO> list = scoreMapper.getB30(uid);
		return list;
	}

	@Override
	public UserPTTVO updatePTT() {
		//获取用户和基本信息
		Integer userId = BaseContext.getCurrentId();
		User user = userMapper.getUserByUid(userId);
		Double ptt = user.getPtt();

		//计算ptt
		List<UserB30VO> list = getB30(30);
		double sum = list.stream()
				.mapToDouble(UserB30VO::getPtt)
				.sum();
		Double b30 = sum / 30.0;
		Double r10 = (40 * ptt - sum) / 10.0;

		//更新用户ptt
		user = new User();
		user.setPttB30(b30);
		user.setPttR10(r10);
		user.setUid(userId);
		userMapper.update(user);

		//更新ptt历史
		pttHistoryMapper.saveOrUpdate(userId, ptt, b30, r10, LocalDate.now());

		//封装返回值
		UserPTTVO userPTTVO = new UserPTTVO();
		userPTTVO.setPtt(ptt);
		userPTTVO.setPttR10(r10);
		userPTTVO.setPttB30(b30);

		return userPTTVO;
	}

	@Override
	@Transactional
	public void importScore(MultipartFile file){
		if(file == null || file.isEmpty()){
			throw new DataException("用户上传数据为空!");
		}

		Integer uid = BaseContext.getCurrentId();
		log.info("{} 开始导入成绩", uid);

		String downloadLabel = "import";
		String filePath = tempPath + downloadLabel + "/" + uid + ".st3";
		String url = "jdbc:sqlite:" + filePath;
		String sql = "select songId, songDifficulty, score from scores";

		//创建目录
		try {
			Files.createDirectories(Path.of(tempPath + downloadLabel + "/"));
		} catch (Exception e) {
			throw new FileIOException("import文件夹创建失败：", e);
		}

		//保存文件
		try {
			Files.copy(file.getInputStream(), Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new FileIOException("import文件拷贝出现异常：", e);
		}

		try(
				Connection connection = DriverManager.getConnection(url);
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()){

			//获取全部歌曲难度的sgid并排序，后面用来查定数
			List<Song> songs = songMapper.getAllSong();
			songs.sort(Comparator.comparing(Song::getSgid));
			List<String> songsgid = songs.stream().map(Song::getSgid).toList();

			//读出数据
			List<ImportScoreDTO> list = new ArrayList<>();
			while(resultSet.next()){
				ImportScoreDTO dto = new ImportScoreDTO();
				dto.setSgid(resultSet.getString(1));
				dto.setSongDifficulty(resultSet.getInt(2));
				dto.setScore(resultSet.getInt(3));
				list.add(dto);
			}

			//为数据排序并将相同歌曲的不同难度合并成一条记录
			list.sort(Comparator.comparing(ImportScoreDTO::getSgid));
			List<Score> scoreList = new ArrayList<>();
			String lastSgid = "";
			Score score = new Score();
			Song nowSong = null;
			for(ImportScoreDTO dto: list){
				if(!dto.getSgid().equals(lastSgid)){
					scoreList.add(score);
					score = new Score();
					nowSong = songs.get(Collections.binarySearch(songsgid, dto.getSgid()));
					score.setSid(nowSong.getSid());
					score.setUid(uid);
					lastSgid = dto.getSgid();
				}
				switch(dto.getSongDifficulty()){
					case 0:
						score.setPstScore(dto.getScore());
						score.setPstPtt(ArcaeaUtils.pptCalc(dto.getScore(), nowSong.getPst()));
						break;
					case 1:
						score.setPrsScore(dto.getScore());
						score.setPrsPtt(ArcaeaUtils.pptCalc(dto.getScore(), nowSong.getPrs()));
						break;
					case 2:
						score.setFtrScore(dto.getScore());
						score.setFtrPtt(ArcaeaUtils.pptCalc(dto.getScore(), nowSong.getFtr()));
						break;
					case 3:
						score.setBydScore(dto.getScore());
						score.setBydPtt(ArcaeaUtils.pptCalc(dto.getScore(), nowSong.getByd()));
						break;
					case 4:
						score.setEtrScore(dto.getScore());
						score.setEtrPtt(ArcaeaUtils.pptCalc(dto.getScore(), nowSong.getEtr()));
				}
			}
			//去掉第一个加多的
			scoreList.remove(0);

			//导入数据（删除原有全部数据）
			scoreMapper.deleteAllByUid(uid);
			scoreMapper.insertScoreBatch(scoreList);

			//更新ptt
			updatePTT();
		} catch (SQLException e) {
			throw new DataException("用户导入数据，数据出现异常：", e);
		}
	}

	@Override
	public UserPTTVO updateNewPPT(Double newPTT) {
		//更新新的PPT
		Integer userId = BaseContext.getCurrentId();
		User user = new User();
		user.setUid(userId);
		user.setPtt(newPTT);
		userMapper.update(user);
		//计算B30和R10
		return updatePTT();
	}

	@Override
	public UserScoreVO getScoreBySgid(String sgid) {
		return scoreMapper.getScoreBySgid(sgid, BaseContext.getCurrentId());
	}

	@Override
	public void exportScoreWithCsv(){
		Integer userId = BaseContext.getCurrentId();
		//读取成绩
		List<UserScoreVO> scores = scoreMapper.getAllScore(userId);
		Map<Integer, UserScoreVO> scoreMap = scores.stream()
				.collect(Collectors.toMap(UserScoreVO::getSid, Function.identity()));
		//初始化CSV
		StringWriter sw = new StringWriter();
		CSVFormat csvFormat = CSVFormat.EXCEL.builder()
				.setHeader("sid", "sgid", "sname",
						"pst", "pst_score", "pst_ptt",
						"prs", "prs_score", "prs_ptt",
						"ftr", "ftr_score", "ftr_ptt",
						"byd", "byd_score", "byd_ptt",
						"etr", "etr_score", "etr_ptt")
				.build();
		CSVPrinter csvPrinter = null;
		try {
			csvPrinter = new CSVPrinter(sw, csvFormat);
			//查询所有歌曲
			List<Song> songs = songMapper.getAllSong();
			for(Song song: songs){
				UserScoreVO score = scoreMap.get(song.getSid());
				if(score == null) score = UserScoreVO.empty();
				csvPrinter.printRecord(
						song.getSid(), song.getSgid(), song.getSname(),
						song.getPst(), score.getPstScore(), score.getPstPtt(),
						song.getPrs(), score.getPrsScore(), score.getPrsPtt(),
						song.getFtr(), score.getFtrScore(), score.getFtrPtt(),
						song.getByd(), score.getBydScore(), score.getBydPtt(),
						song.getEtr(), score.getEtrScore(), score.getEtrPtt()
				);
			}
		} catch (IOException e) {
			throw new FileIOException("CSV处理异常！", e);
		}
		//输出文件
		String downloadLabel = "export";
		String fileName = userId + ".csv";
		try {
			Files.createDirectories(Path.of(tempPath + downloadLabel + "/"));
		} catch (Exception e) {
			throw new FileIOException("无法创建export文件夹", e);
		}
		try(FileOutputStream fos = new FileOutputStream(tempPath + downloadLabel + "/" + fileName);
			BufferedOutputStream bos = new BufferedOutputStream(fos)) {
			csvPrinter.flush();
			String csvContent = sw.toString();
			bos.write(csvContent.getBytes());
			bos.close();
			csvPrinter.close();
		} catch (IOException e) {
			throw new FileIOException("Error writing CSV content to file", e);
		}
	}

	@Override
	public void importCsv(MultipartFile file) {
		if(file == null || file.isEmpty()){
			throw new DataException("用户上传数据出错！");
		}

		//基本数据
		Integer userId = BaseContext.getCurrentId();

		//初始化CSV格式
		CSVFormat csvFormat = CSVFormat.EXCEL.builder()
				.setHeader("sid", "sgid", "sname",
						"pst", "pst_score", "pst_ptt",
						"prs", "prs_score", "prs_ptt",
						"ftr", "ftr_score", "ftr_ptt",
						"byd", "byd_score", "byd_ptt",
						"etr", "etr_score", "etr_ptt")
				.build();
		//解析CSV
		try(InputStream is = file.getInputStream()){
			CSVParser parser = CSVParser.parse(is, StandardCharsets.UTF_8, csvFormat);
			List<CSVRecord> records = parser.getRecords();
			List<Score> scoreList = records.stream().filter(r -> r != null && r.getRecordNumber() != 1
							&& (!r.get(4).isEmpty() || !r.get(7).isEmpty() || !r.get(10).isEmpty() || !r.get(13).isEmpty() || !r.get(16).isEmpty()))
					.map(r -> {
						Score score = new Score();
						score.setUid(userId);
						score.setSid(Integer.parseInt(r.get(0)));
						if (!r.get((4)).isEmpty()) {
							int s = Integer.parseInt(r.get(4));
							double d = Double.parseDouble(r.get(3));
							score.setPstScore(s);
							score.setPstPtt(ArcaeaUtils.pptCalc(s, d));
						}
						if (!r.get(7).isEmpty()) {
							int s = Integer.parseInt(r.get(7));
							double d = Double.parseDouble(r.get(6));
							score.setPrsScore(s);
							score.setPrsPtt(ArcaeaUtils.pptCalc(s, d));
						}
						if (!r.get(10).isEmpty()) {
							int s = Integer.parseInt(r.get(10));
							double d = Double.parseDouble(r.get(9));
							score.setFtrScore(s);
							score.setFtrPtt(ArcaeaUtils.pptCalc(s, d));
						}
						if (!r.get(13).isEmpty()) {
							int s = Integer.parseInt(r.get(13));
							double d = Double.parseDouble(r.get(12));
							score.setFtrScore(s);
							score.setFtrPtt(ArcaeaUtils.pptCalc(s, d));
						}
						if (!r.get(16).isEmpty()) {
							int s = Integer.parseInt(r.get(16));
							double d = Double.parseDouble(r.get(15));
							score.setFtrScore(s);
							score.setFtrPtt(ArcaeaUtils.pptCalc(s, d));
						}
						return score;
					}).toList();
			scoreMapper.deleteAllByUid(userId);
			scoreMapper.insertScoreBatch(scoreList);
		} catch (Exception e) {
			throw new RuntimeException("导入CSV异常！", e);
		}
	}


}
