package com.aptc.controller.user;

import com.aptc.exception.UserAuthException;
import com.aptc.exception.UserStatusException;
import com.aptc.pojo.dto.RegisterDTO;
import com.aptc.pojo.dto.UserCountDTO;
import com.aptc.pojo.dto.UserLoginDTO;
import com.aptc.pojo.dto.UserUpdateInfoDTO;
import com.aptc.pojo.vo.UserLoginVO;
import com.aptc.result.Result;
import com.aptc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user/user")
@Slf4j
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/login")
	public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) throws UserStatusException, UserAuthException {
		UserLoginVO userLoginVO = userService.login(userLoginDTO);
		log.info("用户登录：{}", userLoginDTO.getUsername());
		return Result.success(userLoginVO);
	}

	@GetMapping("/current")
	public Result<UserLoginVO> current(){
		UserLoginVO userLoginVO = userService.getCurrent();
		return Result.success(userLoginVO);
	}

	@PostMapping("/register")
	public Result<String> register(@RequestBody RegisterDTO registerDTO){
		userService.register(registerDTO);
		return Result.success();
	}

	@PostMapping("/count")
	public Result<Integer> count(@RequestBody UserCountDTO userCountDTO){
		Integer ret = userService.count(userCountDTO);
		return Result.success(ret);
	}

	@PutMapping("/update")
	public Result<String> updateUserInfo(@RequestBody UserUpdateInfoDTO userUpdateInfoDTO) throws UserAuthException {
		userService.updateUserInfo(userUpdateInfoDTO);
		return Result.success();
	}
}
