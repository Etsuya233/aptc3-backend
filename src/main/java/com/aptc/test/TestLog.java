package com.aptc.test;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestLog {
	@Test
	public void test(){
		Logger logger = LoggerFactory.getLogger(TestLog.class);//这里是字符串或者类，用于日志的前面
		String errorMsg = "my error";
		logger.error("Error: {}", errorMsg);
	}
}
