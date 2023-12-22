package com.aptc.controller.test;

import com.aptc.service.ScoreService;
import com.aptc.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private TestService testService;
	@Autowired
	private ScoreService scoreService;

	@RequestMapping("/test1")
	public void test1(){
		try {
			testService.json2obj1();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@RequestMapping("/timport")
	public void testImport(){
		scoreService.importScore();
	}
}
