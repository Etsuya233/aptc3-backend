package com.aptc.service;

import com.aptc.pojo.dto.UserScoreDTO;
import com.aptc.pojo.dto.UserScoreQueryDTO;
import com.aptc.pojo.vo.UserB30VO;
import com.aptc.pojo.vo.UserPTTVO;
import com.aptc.result.PageResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ScoreService {
	PageResult getAllScore(UserScoreQueryDTO userScoreQueryDTO);

	void updateScore(UserScoreDTO userScoreDTO);

	List<UserB30VO> getB30(Integer pageSize);

	UserPTTVO updatePTT();

	void importScore(MultipartFile file);

	void exportScore(HttpServletResponse response);
}
