package com.aptc.mapper;

import com.aptc.pojo.Score;
import com.aptc.pojo.dto.UserExportSt3VO;
import com.aptc.pojo.dto.UserScoreDTO;
import com.aptc.pojo.dto.UserScoreQueryDTO;
import com.aptc.pojo.vo.UserB30VO;
import com.aptc.pojo.vo.UserScoreVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScoreMapper {
	List<UserScoreVO> userScorePageQuery(UserScoreQueryDTO userScoreQueryDTO);

	void updateScore(UserScoreDTO userScoreDTO);

	void insertScore(Score score);

	List<UserB30VO> getB30(Integer uid);

	@Delete("delete from t_score where uid = #{uid}")
	void deleteAllByUid(Integer uid);

	void insertScoreBatch(List<Score> scores);

	List<UserExportSt3VO> getExportSt3List(Integer uid);

	UserScoreVO getScoreBySgid(String sgid, Integer userId);

	List<UserScoreVO> getAllScore(Integer userId);
}
