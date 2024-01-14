package com.aptc.service;

import com.aptc.pojo.dto.RegisterDTO;
import com.aptc.pojo.dto.UserCountDTO;
import com.aptc.pojo.dto.UserLoginDTO;
import com.aptc.pojo.dto.UserUpdateInfoDTO;
import com.aptc.pojo.vo.UserLoginVO;

public interface UserService {
	UserLoginVO login(UserLoginDTO userLoginDTO);

	UserLoginVO getCurrent();

	void register(RegisterDTO registerDTO);

	Integer count(UserCountDTO userCountDTO);

	void updateUserInfo(UserUpdateInfoDTO userUpdateInfoDTO);
}
