package com.aptc.service.impl;

import com.aptc.mapper.ScoreMapper;
import com.aptc.mapper.UserMapper;
import com.aptc.pojo.User;
import com.aptc.pojo.dto.UserScoreDTO;
import com.aptc.pojo.dto.UserScoreQueryDTO;
import com.aptc.pojo.vo.UserB30VO;
import com.aptc.pojo.vo.UserPTTVO;
import com.aptc.pojo.vo.UserScoreVO;
import com.aptc.result.PageResult;
import com.aptc.service.ScoreService;
import com.aptc.utils.BaseContext;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoreServiceImpl implements ScoreService {
	private final ScoreMapper scoreMapper;
	private final UserMapper userMapper;

	public ScoreServiceImpl(ScoreMapper scoreMapper, UserMapper userMapper) {
		this.scoreMapper = scoreMapper;
		this.userMapper = userMapper;
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


}
