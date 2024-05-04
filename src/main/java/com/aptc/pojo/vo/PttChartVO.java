package com.aptc.pojo.vo;

import com.aptc.service.PttHistoryService;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

@Data
public class PttChartVO {
	private List<String> xAxisData;
	private List<Double> pttData;
	private List<Double> b30Data;
	private List<Double> r10Data;
	private Integer minValue;

	public static PttChartVO empty(){
		PttChartVO pttChartVO = new PttChartVO();
		pttChartVO.setXAxisData(new ArrayList<>());
		pttChartVO.setPttData(new ArrayList<>());
		pttChartVO.setB30Data(new ArrayList<>());
		pttChartVO.setR10Data(new ArrayList<>());
		return pttChartVO;
	}
}
