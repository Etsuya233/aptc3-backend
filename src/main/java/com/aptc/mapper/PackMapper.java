package com.aptc.mapper;

import com.aptc.pojo.Pack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PackMapper {
	@Select("select * from t_pack")
	List<Pack> getAll();
}
