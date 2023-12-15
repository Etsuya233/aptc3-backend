package com.aptc;

import com.aptc.service.ScoreService;
import com.aptc.utils.BaseContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class score {
	private final ScoreService scoreService;

	public score(ScoreService scoreService) {
		this.scoreService = scoreService;
	}

	@Test
	public void test(){
		BaseContext.setCurrentId(1);
		Double v = scoreService.updateB30();
		System.out.println(v);
	}

}
