package com.aptc.pojo.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ChartVO {
	private String date;
	private Double value;

	public static ChartVO of(String date, Double value) {
		ChartVO chartVO = new ChartVO();
		chartVO.setDate(date);
		chartVO.setValue(value);
		return chartVO;
	}
}
