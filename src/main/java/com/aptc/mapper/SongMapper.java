package com.aptc.mapper;

import com.aptc.pojo.Song;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SongMapper {
	@Select("select * from t_song")
	List<Song> getAllSong();

}
