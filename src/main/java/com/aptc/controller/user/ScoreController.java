package com.aptc.controller.user;

import com.aptc.exception.DataException;
import com.aptc.exception.DataProcessingException;
import com.aptc.exception.FileIOException;
import com.aptc.pojo.dto.UserScoreDTO;
import com.aptc.pojo.dto.UserScoreQueryDTO;
import com.aptc.pojo.vo.UserB30VO;
import com.aptc.pojo.vo.UserPTTVO;
import com.aptc.result.PageResult;
import com.aptc.result.Result;
import com.aptc.service.DownloadService;
import com.aptc.service.ScoreService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user/score")
public class ScoreController {

	private ScoreService scoreService;
	private DownloadService downloadService;

	public ScoreController(ScoreService scoreService, DownloadService downloadService) {
		this.scoreService = scoreService;
		this.downloadService = downloadService;
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
	public Result<String> importScore(@RequestParam("file") MultipartFile file) throws DataException, FileIOException {
		scoreService.importScore(file);
		return Result.success();
	}

	//TODO 把下载和生成文件分离 这个是生成st3文件的！
	//TODO 对应的Task也要更改！
	@GetMapping("/export")
	public Result<String> exportScore() throws DataException, FileIOException, DataProcessingException {
		scoreService.exportScore();
		return Result.success();
	}

	@GetMapping("/downloadSt3")
	public void downloadScoreSt3(HttpServletResponse response){
		downloadService.downloadGeneral("export", "st3", response);
	}

	@PutMapping("/newPPT")
	public Result<UserPTTVO> updateNewPPT(@RequestParam Double newPTT){
		UserPTTVO userPTTVO = scoreService.updateNewPPT(newPTT);
		return Result.success(userPTTVO);
	}
}
