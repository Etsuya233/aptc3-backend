package com.aptc.controller.user;

import com.aptc.service.SongService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/user/song")
public class SongController {

	private SongService songService;

	public SongController(SongService songService) {
		this.songService = songService;
	}


}
