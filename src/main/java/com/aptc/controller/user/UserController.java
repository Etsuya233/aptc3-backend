package com.aptc.controller.user;

import com.aptc.pojo.dto.UserLoginDTO;
import com.aptc.pojo.vo.UserLoginVO;
import com.aptc.result.Result;
import com.aptc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/user")
@Slf4j
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/login")
	public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
		UserLoginVO userLoginVO = userService.login(userLoginDTO);
		log.info("用户登录：{}", userLoginDTO.getUsername());
		return Result.success(userLoginVO);
	}


}
