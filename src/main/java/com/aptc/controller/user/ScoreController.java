package com.aptc.controller.user;

import com.aptc.pojo.dto.UserScoreDTO;
import com.aptc.pojo.dto.UserScoreQueryDTO;
import com.aptc.pojo.vo.UserScoreVO;
import com.aptc.result.PageResult;
import com.aptc.result.Result;
import com.aptc.service.ScoreService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/score")
public class ScoreController {

	private ScoreService scoreService;

	public ScoreController(ScoreService scoreService) {
		this.scoreService = scoreService;
	}

	@PostMapping("")
	public Result<PageResult> getAllScore(@RequestBody UserScoreQueryDTO userScoreQueryDTO){
		PageResult pageResult = scoreService.getAllScore(userScoreQueryDTO);
		return Result.success(pageResult);
	}

	@PostMapping("/{sid}")
	public Result<String> updateScore(@PathVariable String sid, @RequestBody UserScoreDTO userScoreDTO){
		scoreService.updateScore(userScoreDTO);
		return Result.success();
	}

}
