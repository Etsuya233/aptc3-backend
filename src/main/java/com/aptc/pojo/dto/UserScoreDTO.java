package com.aptc.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserScoreDTO implements Serializable {
	private Integer sid;
	private Integer scid;
	private Integer pid;
	private String sname;
	private Double pst;
	private Integer pstScore;
	private Double pstPtt;
	private Double prs;
	private Integer prsScore;
	private Double prsPtt;
	private Double ftr;
	private Integer ftrScore;
	private Double ftrPtt;
	private Double byd;
	private Integer bydScore;
	private Double bydPtt;
	private Integer type;
	private Integer uid;
}