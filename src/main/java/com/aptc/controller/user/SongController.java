package com.aptc.controller.user;

import com.aptc.pojo.Song;
import com.aptc.result.Result;
import com.aptc.service.SongService;
import org.apache.ibatis.annotations.ResultType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/user/song")
public class SongController {

	private SongService songService;

	public SongController(SongService songService) {
		this.songService = songService;
	}

	@GetMapping("")
	public Result<List<Song>> getAllSong(){
		List<Song> list = songService.getAllSong();
		return Result.success(list);
	}
}
