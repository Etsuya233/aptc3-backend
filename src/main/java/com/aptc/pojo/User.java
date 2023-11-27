package com.aptc.pojo;

import lombok.Data;

@Data
public class User {
	private Integer uid;
	private String arcId;
	private String username;
	private String password;
	private Integer status;
	private Double ptt;
	private Double pttB30;
	private Double pttR10;
}
