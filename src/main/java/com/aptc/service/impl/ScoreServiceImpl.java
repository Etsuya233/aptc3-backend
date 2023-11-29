package com.aptc.service.impl;

import com.aptc.mapper.ScoreMapper;
import com.aptc.pojo.vo.UserScoreVO;
import com.aptc.result.PageResult;
import com.aptc.service.ScoreService;
import com.aptc.utils.BaseContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {
	private final ScoreMapper scoreMapper;

	public ScoreServiceImpl(ScoreMapper scoreMapper) {
		this.scoreMapper = scoreMapper;
	}

	@Override
	public PageResult getAllScore(Integer pageNum, Integer pageSize) {
		Integer uid = BaseContext.getCurrentId();
		PageHelper.startPage(pageNum, pageSize);
		List<UserScoreVO> list = scoreMapper.userScorePageQuery(uid, pageNum, pageSize);
		PageInfo<UserScoreVO> pageInfo = PageInfo.of(list);

		PageResult pageResult = new PageResult();
		pageResult.setRecords(list);
		pageResult.setTotal((int) pageInfo.getTotal());

		return pageResult;
	}
}
