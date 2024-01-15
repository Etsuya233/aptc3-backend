package com.aptc.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@Component
public class Task {
	@Value("${ety.path.import}")
	private String importTempPath;
	@Value("${ety.path.export}")
	private String exportTempPath;

	@Scheduled(cron = "0 0 4 * * ?")
	public void deleteImportTempFile(){
		try {
			Files.walkFileTree(Path.of(importTempPath), new SimpleFileVisitor<Path>(){
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("文件删除失败！Import！");
		}
	}

	@Scheduled(cron = "0 0 4 * * ?")
	public void deleteExportTempFile(){
		try {
			Files.walkFileTree(Path.of(exportTempPath), new SimpleFileVisitor<Path>(){
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("文件删除失败！Export！");
		}
	}

}
