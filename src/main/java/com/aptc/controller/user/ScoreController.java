package com.aptc.controller.user;

import com.aptc.pojo.dto.ImportScoreDTO;
import com.aptc.pojo.dto.UserScoreDTO;
import com.aptc.pojo.dto.UserScoreQueryDTO;
import com.aptc.pojo.vo.UserB30VO;
import com.aptc.pojo.vo.UserPTTVO;
import com.aptc.pojo.vo.UserScoreVO;
import com.aptc.result.PageResult;
import com.aptc.result.Result;
import com.aptc.service.ScoreService;
import com.github.pagehelper.PageHelper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

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
	public Result<String> updateScore(@RequestBody UserScoreDTO userScoreDTO){
		scoreService.updateScore(userScoreDTO);
		return Result.success();
	}

	@GetMapping("/b30")
	public Result<List<UserB30VO>> getB30(@RequestParam(defaultValue = "30") Integer pageSize){
		List<UserB30VO> list = scoreService.getB30(pageSize);
		return Result.success(list);
	}

	@PutMapping("/ptt")
	public Result<UserPTTVO> updatePTT(){ //这个叫refresh ppt更合适
		UserPTTVO userPTTVO = scoreService.updatePTT();
		return Result.success(userPTTVO);
	}

	@PostMapping("/import")
	public Result<String> importScore(@RequestParam("file") MultipartFile file){
		scoreService.importScore(file);
		return Result.success();
	}


	@GetMapping("/export")
	public void exportScore(HttpServletResponse response){
		scoreService.exportScore(response);
	}

	@PutMapping("/newPPT")
	public Result<UserPTTVO> updateNewPPT(@RequestParam Double newPTT){
		UserPTTVO userPTTVO = scoreService.updateNewPPT(newPTT);
		return Result.success(userPTTVO);
	}
}
