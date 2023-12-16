package com.aptc.mapper;

import com.aptc.pojo.User;
import com.aptc.pojo.dto.RegisterDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RestController;

@Mapper
public interface UserMapper {

	@Select("select * from t_user where username = #{username} ")
	User getUserByUsername(String username);

	@Select("select * from t_user where uid = #{currentId} ")
	User getUserByUid(Integer currentId);


	void update(User user);

	void insert(RegisterDTO registerDTO);
}

