package com.aptc.service;

import com.aptc.pojo.dto.UserLoginDTO;
import com.aptc.pojo.vo.UserLoginVO;

public interface UserService {
	UserLoginVO login(UserLoginDTO userLoginDTO);
}
