package com.aptc.service;

import com.aptc.pojo.vo.ChartVO;
import com.aptc.pojo.vo.PttChartVO;

import java.util.List;

public interface PttHistoryService {
	PttChartVO getPttCharts(String beginTimeStr, String endTimeStr);
}
