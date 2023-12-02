package com.aptc.mapper;

import com.aptc.pojo.dto.UserScoreQueryDTO;
import com.aptc.pojo.vo.UserScoreVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScoreMapper {
	List<UserScoreVO> userScorePageQuery(UserScoreQueryDTO userScoreQueryDTO);
}
