package com.aptc.mapper;

import com.aptc.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RestController;

@Mapper
public interface UserMapper {

	@Select("select * from t_user where username = #{username} ")
	User getUserByUsername(String username);

}
