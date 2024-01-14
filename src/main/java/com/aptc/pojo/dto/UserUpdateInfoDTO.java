package com.aptc.pojo.dto;

import lombok.Data;

@Data
public class UserUpdateInfoDTO {
	private String username;
	private String oldpw;
	private String newpw;
}
