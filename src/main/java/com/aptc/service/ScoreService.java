package com.aptc.service;

import com.aptc.exception.DataException;
import com.aptc.exception.DataProcessingException;
import com.aptc.exception.FileIOException;
import com.aptc.pojo.dto.UserScoreDTO;
import com.aptc.pojo.dto.UserScoreQueryDTO;
import com.aptc.pojo.vo.UserB30VO;
import com.aptc.pojo.vo.UserPTTVO;
import com.aptc.pojo.vo.UserScoreVO;
import com.aptc.result.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ScoreService {
	PageResult getAllScore(UserScoreQueryDTO userScoreQueryDTO);

	void updateScore(UserScoreDTO userScoreDTO);

	List<UserB30VO> getB30(Integer pageSize);

	UserPTTVO updatePTT();

	void importScore(MultipartFile file);

	UserPTTVO updateNewPPT(Double newPTT);

	UserScoreVO getScoreBySgid(String sgid);

	void exportScoreWithCsv();

	void importCsv(MultipartFile file);
}
