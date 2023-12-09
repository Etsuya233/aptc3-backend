package com.aptc.service;

import com.aptc.pojo.dto.UserScoreDTO;
import com.aptc.pojo.dto.UserScoreQueryDTO;
import com.aptc.result.PageResult;

public interface ScoreService {
	PageResult getAllScore(UserScoreQueryDTO userScoreQueryDTO);

	void updateScore(UserScoreDTO userScoreDTO);
}
