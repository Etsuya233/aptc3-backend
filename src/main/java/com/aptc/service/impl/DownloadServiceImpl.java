package com.aptc.service.impl;

import com.aptc.service.DownloadService;
import com.aptc.utils.BaseContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
@Slf4j
public class DownloadServiceImpl implements DownloadService {

	@Value("${ety.path.temp}")
	private String tempPath;

	public void downloadGeneral(String downloadLabel, String fileExtension, HttpServletResponse response){
		Integer userId = BaseContext.getCurrentId();
		String fileName = userId + "." + fileExtension;
		String fullPath = tempPath + downloadLabel + "/" + fileName;

		File file = new File(fullPath);

		// 设置响应头
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		response.addHeader("Content-Length", String.valueOf(file.length()));

		// 读取文件并写入响应流
		try (FileInputStream fis = new FileInputStream(file);
			 OutputStream os = response.getOutputStream()) {

			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			log.error("用户下载st3出错：{}", userId, e);
		}
	}
}
