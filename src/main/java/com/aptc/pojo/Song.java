package com.aptc.pojo;

import lombok.Data;

@Data
public class Song {
	private Integer sid;
	private Integer pid;
	private String sname;
	private String alias;
	private Double pst;
	private Double prs;
	private Double ftr;
	private Double byd;
	private String sgid;
}