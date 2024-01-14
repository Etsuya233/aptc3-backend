package com.aptc.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
public class UserLoginVO {
	private String token;
	private String arcId;
	private Integer uid;
	private String username;
	private Integer status;
	private Double ptt;
	private Double pttB30;
	private Double pttR10;
}
