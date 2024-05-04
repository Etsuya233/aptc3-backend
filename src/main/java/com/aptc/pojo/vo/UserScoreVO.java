package com.aptc.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserScoreVO implements Serializable {
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
	private Double etr;
	private Integer etrScore;
	private Double etrPtt;

	public static UserScoreVO empty(){
		UserScoreVO userScoreVO = new UserScoreVO();
		userScoreVO.setPstScore(0);
		userScoreVO.setPrsScore(0);
		userScoreVO.setFtrScore(0);
		userScoreVO.setBydScore(0);
		userScoreVO.setEtrScore(0);
		userScoreVO.setPstPtt(0.0);
		userScoreVO.setPrsPtt(0.0);
		userScoreVO.setFtrPtt(0.0);
		userScoreVO.setBydPtt(0.0);
		userScoreVO.setEtrPtt(0.0);
		return userScoreVO;
	}

}
