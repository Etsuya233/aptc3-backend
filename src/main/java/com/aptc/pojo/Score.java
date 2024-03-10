package com.aptc.pojo;

import lombok.Data;

@Data
public class Score {
	private Integer scid;
	private Integer uid;
	private Integer sid;
	private Integer pstScore;
	private Integer prsScore;
	private Integer ftrScore;
	private Integer bydScore;
	private Integer etrScore;
	private Double pstPtt;
	private Double prsPtt;
	private Double ftrPtt;
	private Double bydPtt;
	private Double etrPtt;
}
