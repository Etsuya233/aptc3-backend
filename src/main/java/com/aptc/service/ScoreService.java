package com.aptc.service;

import com.aptc.result.PageResult;

public interface ScoreService {
	PageResult getAllScore(Integer pageNum, Integer pageSize);
}
