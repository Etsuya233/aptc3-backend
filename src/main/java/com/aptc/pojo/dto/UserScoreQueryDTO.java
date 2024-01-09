package com.aptc.pojo.dto;

import lombok.Data;

import java.io.Serializable;

//TODO: sname pid is useless?

@Data
public class UserScoreQueryDTO implements Serializable {
	private Integer uid;
	private String sname;
	private String pid;
	private Integer pageNum;
	private Integer pageSize;
}
