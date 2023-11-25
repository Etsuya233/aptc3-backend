package com.aptc.controller.user;

import com.aptc.pojo.vo.ScoreListUser;
import com.aptc.result.PageResult;
import com.aptc.result.Result;
import com.aptc.service.SongService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/song")
public class SongController {

	private SongService songService;

	public SongController(SongService songService) {
		this.songService = songService;
	}

	public Result<PageResult> getScoreView(Integer pageNum, Integer pageSize){
		PageResult ret = songService.queryScore(pageNum, pageSize);
		return Result.success(ret);
	}
}
