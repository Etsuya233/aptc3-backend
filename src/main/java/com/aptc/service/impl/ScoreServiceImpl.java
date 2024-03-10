package com.aptc.service.impl;

import com.aptc.exception.DataException;
import com.aptc.exception.DataProcessingException;
import com.aptc.exception.FileIOException;
import com.aptc.mapper.ScoreMapper;
import com.aptc.mapper.SongMapper;
import com.aptc.mapper.UserMapper;
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
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScoreServiceImpl implements ScoreService {
	private final ScoreMapper scoreMapper;
	private final UserMapper userMapper;
	private final SongMapper songMapper;

	@Value("${ety.path.temp}")
	private String tempPath;

	public ScoreServiceImpl(ScoreMapper scoreMapper, UserMapper userMapper, SongMapper songMapper) {
		this.scoreMapper = scoreMapper;
		this.userMapper = userMapper;
		this.songMapper = songMapper;
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
		Integer userId = BaseContext.getCurrentId();

		User user = userMapper.getUserByUid(userId);
		UserPTTVO userPTTVO = new UserPTTVO();
		userPTTVO.setPtt(user.getPtt());

		List<UserB30VO> list = getB30(30);
		DoubleSummaryStatistics stat = list.stream().collect(Collectors.summarizingDouble(UserB30VO::getPtt));
		Double b30 = stat.getSum() / 30.0;
		Double r10 = (40 * user.getPtt() - stat.getSum()) / 10.0;

		user = new User();
		user.setPttB30(b30);
		user.setPttR10(r10);
		user.setUid(userId);
		userMapper.update(user);

		userPTTVO.setPttR10(r10);
		userPTTVO.setPttB30(b30);


		return userPTTVO;
	}

	@Override
	@Transactional
	public void importScore(MultipartFile file) throws FileIOException, DataException {
		if(file.isEmpty()){
			throw new DataException("用户上传数据为空!");
		}

		//TODO 应该要一个生成图！
		Integer uid = BaseContext.getCurrentId();
		String downloadLabel = "import";
		String filePath = tempPath + downloadLabel + "/" + uid + ".st3";
		String url = "jdbc:sqlite:" + filePath;
		String sql = "select songId, songDifficulty, score from scores";


		//使用createDirectories，父目录不存在仍可以创建！
		try {
			Files.createDirectories(Path.of(tempPath + downloadLabel + "/"));
			log.info("import已成功创建");
		} catch (FileAlreadyExistsException e) {
			log.info("import文件夹已存在，不再创建");
		} catch (IOException e) {
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
					System.out.println(dto.getSgid());
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
	public void exportScore() throws FileIOException, DataProcessingException {
		Integer userId = BaseContext.getCurrentId();
		String downloadLabel = "export";
		String fileName = userId + ".st3";

		try {
			//使用createDirectories，父目录不存在仍可以创建！
			Files.createDirectories(Path.of(tempPath + downloadLabel + "/"));
		} catch(FileAlreadyExistsException e){
			log.info("export文件夹已存在，无需创建。");
		} catch (IOException e) {
			throw new FileIOException("无法创建export文件夹", e);
		}

		try {
			Files.deleteIfExists(Path.of(tempPath + downloadLabel + "/" + fileName));
		} catch (IOException e) {
			throw new FileIOException("无法删除原有文件", e);
		}

		String url = "jdbc:sqlite:" + tempPath + downloadLabel + "/" + fileName;
		String sqlForCreate = "CREATE TABLE \"scores\" (\n" +
				"\t\"id\" INTEGER NOT NULL,\n" +
				"\t\"version\" INTEGER NULL,\n" +
				"\t\"score\" INTEGER NULL,\n" +
				"\t\"shinyPerfectCount\" INTEGER NULL,\n" +
				"\t\"perfectCount\" INTEGER NULL,\n" +
				"\t\"nearCount\" INTEGER NULL,\n" +
				"\t\"missCount\" INTEGER NULL,\n" +
				"\t\"date\" INTEGER NULL,\n" +
				"\t\"songId\" TEXT NULL,\n" +
				"\t\"songDifficulty\" INTEGER NULL,\n" +
				"\t\"modifier\" INTEGER NULL,\n" +
				"\t\"health\" INTEGER NULL,\n" +
				"\t\"ct\" INTEGER NULL,\n" +
				"\tPRIMARY KEY (\"id\")\n" +
				")\n" +
				";";

		try {
			Connection connection = DriverManager.getConnection(url);

			//创建表
			PreparedStatement createStatement = connection.prepareStatement(sqlForCreate);
			createStatement.execute();

			//获取数据
			UserScoreQueryDTO userScoreQueryDTO = new UserScoreQueryDTO();
			userScoreQueryDTO.setUid(userId);
			userScoreQueryDTO.setPageSize(1000);
			userScoreQueryDTO.setPageNum(1);
			List<UserExportSt3VO> scores = scoreMapper.getExportSt3List(userId);

			//导入数据
			if(scores.isEmpty()) {
				return;
			}

			int cnt = 1;
			StringBuilder sb = new StringBuilder("insert into scores (id, score, songId, songDifficulty) values ");
			for(UserExportSt3VO a: scores){
				if(a.getPstScore() != null) sb.append("(").append(cnt++).append(',').append(a.getPstScore()).append(",").append('\'').append(a.getSongId()).append('\'').append(",").append("0").append("),");
				if(a.getPrsScore() != null) sb.append("(").append(cnt++).append(',').append(a.getPrsScore()).append(",").append('\'').append(a.getSongId()).append('\'').append(",").append("1").append("),");
				if(a.getFtrScore() != null) sb.append("(").append(cnt++).append(',').append(a.getFtrScore()).append(",").append('\'').append(a.getSongId()).append('\'').append(",").append("2").append("),");
				if(a.getBydScore() != null) sb.append("(").append(cnt++).append(',').append(a.getBydScore()).append(",").append('\'').append(a.getSongId()).append('\'').append(",").append("3").append("),");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(";");
			System.out.println(sb);
			PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
			preparedStatement.execute();

		} catch (SQLException e) {
			throw new DataProcessingException("导出数据时出现异常", e);
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
		UserPTTVO userPTTVO = updatePTT();
		return userPTTVO;
	}

	@Override
	public UserScoreVO getScoreBySgid(String sgid) {
		return scoreMapper.getScoreBySgid(sgid, BaseContext.getCurrentId());
	}

	@Override
	public void exportScoreWithCsv() throws IOException, FileIOException {
		Integer userId = BaseContext.getCurrentId();
		//读取成绩
		List<UserScoreVO> scores = scoreMapper.getAllScore(userId);
		//导出CSV
		StringWriter sw = new StringWriter();
		CSVFormat csvFormat = CSVFormat.EXCEL.builder()
				.setHeader("sid", "sname",
						"pst", "pst_score", "pst_ptt",
						"prs", "prs_score", "prs_ptt",
						"ftr", "ftr_score", "ftr_ptt",
						"byd", "byd_score", "byd_ptt",
						"etr", "etr_score", "etr_ptt")
				.build();
		CSVPrinter csvPrinter = new CSVPrinter(sw, csvFormat);
		for (UserScoreVO score : scores) {
			csvPrinter.printRecord(
					score.getSid(), score.getSname(),
					score.getPst(),
					score.getPstScore(), score.getPstPtt(),
					score.getPrs(), score.getPrsScore(), score.getPrsPtt(),
					score.getFtr(), score.getFtrScore(), score.getFtrPtt(),
					score.getByd(), score.getBydScore(), score.getBydPtt(),
					score.getEtr(), score.getEtrScore(), score.getEtrPtt()
			);
		}
		//输出文件
		String downloadLabel = "export";
		String fileName = userId + ".csv";
		try {
			//使用createDirectories，父目录不存在仍可以创建！
			Files.createDirectories(Path.of(tempPath + downloadLabel + "/"));
		} catch(FileAlreadyExistsException e){
			log.info("export文件夹已存在，无需创建。");
		} catch (IOException e) {
			throw new FileIOException("无法创建export文件夹", e);
		}
		FileOutputStream fos = new FileOutputStream(tempPath + downloadLabel + "/" + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		try {
			csvPrinter.flush();
			String csvContent = sw.toString();
			bos.write(csvContent.getBytes());
			bos.close();
			csvPrinter.close();
		} catch (IOException e) {
			throw new FileIOException("Error writing CSV content to file", e);
		}
	}


	private void deleteFile(String filePath) throws IOException {
		// 删除文件
		Files.delete(Path.of(filePath));
	}
}
