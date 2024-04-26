package com.aptc.service.impl;

import com.aptc.controller.user.UserController;
import com.aptc.mapper.PttHistoryMapper;
import com.aptc.pojo.PttHistory;
import com.aptc.pojo.vo.ChartVO;
import com.aptc.pojo.vo.PttChartVO;
import com.aptc.service.PttHistoryService;
import com.aptc.utils.BaseContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PttHistoryServiceImpl implements PttHistoryService {

	private final PttHistoryMapper pttHistoryMapper;
	private final UserController userController;

	@Override
	public PttChartVO getPttCharts(String beginTimeStr, String endTimeStr) {
		LocalDate beginTime = LocalDate.parse(beginTimeStr, DateTimeFormatter.ISO_DATE);
		LocalDate endTime = LocalDate.parse(endTimeStr, DateTimeFormatter.ISO_DATE);

		//计算数据间隔
		long dateSpan = endTime.toEpochDay() - beginTime.toEpochDay();
		int interval = (int) (dateSpan / (365 * 2)) + 1;

		//获取数据
		Integer userId = BaseContext.getCurrentId();
		List<PttHistory> pttHistory = pttHistoryMapper.getPttHistory(userId, beginTime, endTime);
		if(pttHistory == null || pttHistory.isEmpty()){
			return PttChartVO.empty();
		}
		//封装数据
		List<String> xAxisData = new ArrayList<>();
		List<Double> pttData = new ArrayList<>();
		List<Double> b30Data = new ArrayList<>();
		List<Double> r10Data = new ArrayList<>();
		LocalDate now = pttHistory.get(0).getTime();
		for(int pointer = 0; ; ){
			PttHistory history = pttHistory.get(pointer);
			String date = now.format(DateTimeFormatter.ISO_DATE);
			xAxisData.add(date);
			pttData.add(history.getPtt());
			b30Data.add(history.getB30());
			r10Data.add(history.getR10());
			if(pointer == pttHistory.size() - 1){
				break;
			}
			//移动指针
			now = now.plusDays(interval);
			if(!pttHistory.get(pointer + 1).getTime().isAfter(now)){
				pointer++;
			}
		}

		//返回数据
		PttChartVO pttChartVO = new PttChartVO();
		pttChartVO.setXAxisData(xAxisData);
		pttChartVO.setPttData(pttData);
		pttChartVO.setB30Data(b30Data);
		pttChartVO.setR10Data(r10Data);

		return pttChartVO;
	}
}
