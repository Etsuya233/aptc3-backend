package com.aptc.mapper;

import com.aptc.pojo.Song;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SongMapper {
	@Select("select * from t_song")
	List<Song> getAllSong();

	@Update("update t_song set ftr = #{difficulty} where sname = #{sname}")
	int testChangeFtrDiff(String sname, Double difficulty);

}
