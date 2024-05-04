package com.aptc.test.cover;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;

@SpringBootTest
public class BatchCoverArtsTest {

	@Test
	public void testCopy(){
		String base = "/home/etsuya/temp/arc/songsData/songs";
		String target = "/home/etsuya/temp/arc/ext";
		File baseDir = new File(base);
		File targetDir = new File(target);
		File[] files = baseDir.listFiles();
		assert files != null;
		ArrayList<Object> failed = new ArrayList<>();
		for(File songDir : files){
			String name = songDir.getName();
			if(name.startsWith("dl_")) name = name.substring(3);
			File cover = new File(songDir, "1080_base_256.jpg");
			if(!cover.exists()){
				cover = new File(songDir, "base_256.jpg");
			}
			try (FileOutputStream fos = new FileOutputStream(new File(target, name + ".jpg"));
				 FileInputStream fis = new FileInputStream(cover);
				 BufferedOutputStream bos = new BufferedOutputStream(fos);
				 BufferedInputStream bis = new BufferedInputStream(fis)){
				if(!cover.exists()){
					failed.add(name);
					continue;
				}
				byte[] buffer = new byte[1024];
				int len;
				while((len = bis.read(buffer)) != -1){
					bos.write(buffer, 0, len);
				}
			} catch (Exception e) {
				failed.add(name);
			}
		}
		failed.forEach(System.out::println);
	}
}
