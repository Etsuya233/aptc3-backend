package com.aptc.mapper;

import com.aptc.pojo.PttHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface PttHistoryMapper {
	int saveOrUpdate(
			@Param("uid") int uid,
			@Param("ptt") double ptt,
			@Param("b30") double b30,
			@Param("r10") double r10,
			@Param("time")LocalDate date);

	List<PttHistory> getPttHistory(@Param("uid") Integer uid,
								   @Param("beginTime") LocalDate beginTime,
								   @Param("endTime") LocalDate endTime);

	@Select("select * from t_ptt_history where uid = #{uid} and time < #{beginTime} order by time desc limit 1")
	PttHistory getLatestBeforeDate(@Param("uid") Integer uid,
								   @Param("beginTime") LocalDate beginTime);
}
