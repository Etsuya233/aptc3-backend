package com.aptc.service;

import com.aptc.pojo.dto.UserScoreDTO;
import com.aptc.pojo.dto.UserScoreQueryDTO;
import com.aptc.pojo.vo.UserB30VO;
import com.aptc.pojo.vo.UserPTTVO;
import com.aptc.result.PageResult;

import java.util.List;

public interface ScoreService {
	PageResult getAllScore(UserScoreQueryDTO userScoreQueryDTO);

	void updateScore(UserScoreDTO userScoreDTO);

	List<UserB30VO> getB30(Integer pageSize);

	UserPTTVO updatePTT();

}
