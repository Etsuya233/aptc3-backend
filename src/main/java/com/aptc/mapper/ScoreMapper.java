package com.aptc.mapper;

import com.aptc.pojo.vo.ScoreListUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScoreMapper {

	public List<ScoreListUser> scorePageQuery(Integer uid);
}
