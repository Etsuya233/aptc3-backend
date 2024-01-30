package com.aptc.service;

import jakarta.servlet.http.HttpServletResponse;

public interface DownloadService {
	public void downloadGeneral(String downloadLabel, String fileExtension, HttpServletResponse response);
}
