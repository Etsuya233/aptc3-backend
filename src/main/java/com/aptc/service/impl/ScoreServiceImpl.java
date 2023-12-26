package com.aptc.service.impl;

import com.aptc.mapper.ScoreMapper;
import com.aptc.mapper.SongMapper;
import com.aptc.mapper.UserMapper;
import com.aptc.pojo.Score;
import com.aptc.pojo.Song;
import com.aptc.pojo.User;
import com.aptc.pojo.dto.ImportScoreDTO;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoreServiceImpl implements ScoreService {
	private final ScoreMapper scoreMapper;
	private final UserMapper userMapper;
	private final SongMapper songMapper;

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
		User user = userMapper.getUserByUid(BaseContext.getCurrentId());
		UserPTTVO userPTTVO = new UserPTTVO();
		userPTTVO.setPtt(user.getPtt());

		List<UserB30VO> list = getB30(30);
		DoubleSummaryStatistics stat = list.stream().collect(Collectors.summarizingDouble(UserB30VO::getPtt));
		Double b30 = stat.getSum() / 30.0;
		Double r10 = (40 * user.getPtt() - stat.getSum()) / 10.0;

		user = new User();
		user.setPttB30(b30);
		user.setPttR10(r10);
		userMapper.update(user);

		userPTTVO.setPttR10(r10);
		userPTTVO.setPttB30(b30);


		return userPTTVO;
	}

	@Override
	@Transactional
	public void importScore(MultipartFile file) {
		if(file.isEmpty()){
			throw new RuntimeException("文件为空！");
		}

		//TODO 优化路径！
		Integer uid = BaseContext.getCurrentId();
		String tempDirectory = "D:/Etsuya/Programming/temp/";
		String filePath = tempDirectory + uid + ".st3";
		String url = "jdbc:sqlite:" + filePath;
		String sql = "select songId, songDifficulty, score from scores";

		//保存文件
		try {
			Files.copy(file.getInputStream(), Path.of(filePath), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException(e);
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
				}
			}
			//去掉第一个加多的
			scoreList.remove(0);

			//导入数据（删除原有全部数据）
			scoreMapper.deleteAllByUid(uid);
			scoreMapper.insertScoreBatch(scoreList);

			//更新ptt
			updatePTT();
		} catch (Exception e){
			throw new RuntimeException("导出数据时出错！");
		}

		//删除文件
		try {
			deleteFile(filePath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void deleteFile(String filePath) throws IOException {
		// 删除文件
		Files.delete(Path.of(filePath));
	}
}
