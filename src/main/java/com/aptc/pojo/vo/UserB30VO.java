package com.aptc.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserB30VO {
	private String sname;
	private Integer difficultyType;
	private Integer score;
	private Double ptt;
	private String difficulty;
	private LocalDateTime time;
	private String sgid;
}
