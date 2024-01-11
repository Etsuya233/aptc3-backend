package com.aptc.service.impl;

import com.aptc.configuration.JwtProperties;
import com.aptc.exception.LoginException;
import com.aptc.exception.UserException;
import com.aptc.mapper.UserMapper;
import com.aptc.pojo.User;
import com.aptc.pojo.dto.RegisterDTO;
import com.aptc.pojo.dto.UserCountDTO;
import com.aptc.pojo.dto.UserLoginDTO;
import com.aptc.pojo.dto.UserUpdateInfoDTO;
import com.aptc.pojo.vo.UserLoginVO;
import com.aptc.service.UserService;
import com.aptc.utils.BaseContext;
import com.aptc.utils.JwtUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

	private UserMapper userMapper;
	private JwtProperties jwtProperties;

	public UserServiceImpl(UserMapper userMapper, JwtProperties jwtProperties) {
		this.userMapper = userMapper;
		this.jwtProperties = jwtProperties;
	}

	@Override
	public UserLoginVO login(UserLoginDTO userLoginDTO) {
		String username = userLoginDTO.getUsername();
		String password = userLoginDTO.getPassword();

		//登录
		User user = userMapper.getUserByUsername(username);
		if(user == null) throw new LoginException("用户不存在！");
		String realPassword = user.getPassword();
		password = DigestUtils.md5DigestAsHex(password.getBytes());
		if(!password.equals(realPassword)) throw new LoginException("密码错误！");
		if(user.getStatus() == 0) throw new LoginException("用户状态异常！");

		//创建JWT令牌
		Map<String, String> claims = new HashMap<>();
		claims.put("currentID", String.valueOf(user.getUid()));

		String token = JwtUtils.createJwt(jwtProperties.getUserKeyObj(), jwtProperties.getUserTtl(), claims);

		UserLoginVO userLoginVO = new UserLoginVO();
		BeanUtils.copyProperties(user, userLoginVO);
		userLoginVO.setToken(token);

		return userLoginVO;
	}

	@Override
	public UserLoginVO getCurrent() {
		User user = userMapper.getUserByUid(BaseContext.getCurrentId());
		UserLoginVO userLoginVO = new UserLoginVO();
		BeanUtils.copyProperties(user, userLoginVO);
		return userLoginVO;
	}

	@Override
	public void register(RegisterDTO registerDTO) {
		String password = registerDTO.getPassword();
		String md5password = DigestUtils.md5DigestAsHex(password.getBytes());
		registerDTO.setPassword(md5password);
		userMapper.insert(registerDTO);
	}

	@Override
	public Integer count(UserCountDTO userCountDTO) {
		return userMapper.count(userCountDTO);
	}

	@Override
	@Transactional
	public void updateUserInfo(UserUpdateInfoDTO userUpdateInfoDTO) {
		User user = userMapper.getUserByUid(BaseContext.getCurrentId());
		//更改用户名
		if(userUpdateInfoDTO.getUsername() != null && !userUpdateInfoDTO.getUsername().isEmpty()){
			user.setUsername(userUpdateInfoDTO.getUsername());
		}
		//更改密码
		if(userUpdateInfoDTO.getOldpw() != null && !userUpdateInfoDTO.getOldpw().isEmpty()){
			//检验密码是否匹配
			//TODO: 异常处理复习
			String oldpw = DigestUtils.md5DigestAsHex(userUpdateInfoDTO.getOldpw().getBytes());
			if(!oldpw.equals(user.getPassword())) throw new UserException("修改用户信息：旧密码错误");
			String newpw = DigestUtils.md5DigestAsHex(userUpdateInfoDTO.getNewpw().getBytes());
			user.setPassword(newpw);
		}
		//更新用户
		userMapper.update(user);
	}
}
