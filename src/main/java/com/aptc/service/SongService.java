package com.aptc.service;

import com.aptc.pojo.vo.ScoreListUser;
import com.aptc.result.PageResult;

public interface SongService {
	PageResult queryScore(Integer pageNum, Integer pageSize);
}
