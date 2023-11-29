package com.aptc.controller.user;

import com.aptc.pojo.vo.UserScoreVO;
import com.aptc.result.PageResult;
import com.aptc.result.Result;
import com.aptc.service.ScoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/score")
public class ScoreController {

	private ScoreService scoreService;

	public ScoreController(ScoreService scoreService) {
		this.scoreService = scoreService;
	}

	@GetMapping("")
	public Result<PageResult> getAllScore(Integer pageNum, Integer pageSize){
		System.out.println("score: " + Thread.currentThread());
		PageResult pageResult = scoreService.getAllScore(pageNum, pageSize);
		return Result.success(pageResult);
	}
}
