package com.aptc.test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void json2obj1() throws IOException {
		//读取JSON文件到Java中
		Path path = Paths.get("C:/Users/etsuy/Downloads/songlist.json");
		List<String> strings = Files.readAllLines(path);
		String str = String.join(System.lineSeparator(), strings);

		//转化为对象
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JsonNode jsonNode = objectMapper.readTree(str);

		JsonNode jsonSongs = jsonNode.get("songs");
		List<Song> songs = new ArrayList<>();
		for(JsonNode jsonSong: jsonSongs){
			Song song = objectMapper.treeToValue(jsonSong, Song.class);
			song.setSname(song.getTitleLocalized().getSname());
			songs.add(song);
		}
		System.out.println(songs);

		//插入数据库
		for(Song song: songs){
			jdbcTemplate.update("update t_song set sgid = ? where sname = ? and not exists (select * from t_song where sgid = ?)", song.getGid(), song.getSname(), song.getGid());
		}

		
	}
}
