package com.aptc.service;

import com.aptc.pojo.vo.PttChartVO;

import java.time.LocalDateTime;

public interface PttHistoryService {
	PttChartVO getPttCharts(LocalDateTime beginDateTime, LocalDateTime endDateTime);
}
