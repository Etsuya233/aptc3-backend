package com.aptc.mapper;

import com.aptc.pojo.User;
import com.aptc.pojo.dto.RegisterDTO;
import com.aptc.pojo.dto.UserCountDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

	@Select("select * from t_user where username = #{username} ")
	User getUserByUsername(String username);

	@Select("select * from t_user where uid = #{currentId} ")
	User getUserByUid(Integer currentId);


	void update(User user);

	void insert(RegisterDTO registerDTO);

	Integer count(UserCountDTO userCountDTO);
}

