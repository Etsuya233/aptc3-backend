package com.aptc.service.impl;

import com.aptc.mapper.ScoreMapper;
import com.aptc.mapper.SongMapper;
import com.aptc.pojo.Song;
import com.aptc.service.SongService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {
	private final ScoreMapper scoreMapper;
	private final SongMapper songMapper;

	public SongServiceImpl(ScoreMapper scoreMapper, SongMapper songMapper) {
		this.scoreMapper = scoreMapper;
		this.songMapper = songMapper;
	}

	@Override
	public List<Song> getAllSong() {
		return songMapper.getAllSong();
	}
}
