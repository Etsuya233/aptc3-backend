package com.aptc.service.impl;

import com.aptc.mapper.ScoreMapper;
import com.aptc.result.PageResult;
import com.aptc.service.SongService;
import com.aptc.utils.BaseContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {
	private ScoreMapper scoreMapper;

	public SongServiceImpl(ScoreMapper scoreMapper) {
		this.scoreMapper = scoreMapper;
	}


}
