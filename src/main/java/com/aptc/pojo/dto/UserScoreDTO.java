package com.aptc.pojo.dto;

import com.aptc.pojo.Score;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserScoreDTO extends Score implements Serializable {
	private Integer pid;
	private Integer type;
}