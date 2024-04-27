package com.aptc.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.aptc.controller.user.UserController;
import com.aptc.mapper.PttHistoryMapper;
import com.aptc.pojo.PttHistory;
import com.aptc.pojo.vo.PttChartVO;
import com.aptc.service.PttHistoryService;
import com.aptc.utils.BaseContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PttHistoryServiceImpl implements PttHistoryService {

	private final PttHistoryMapper pttHistoryMapper;
	private final UserController userController;

	@Override
	public PttChartVO getPttCharts(LocalDateTime beginDateTime, LocalDateTime endDateTime) {
		LocalDate beginTime = beginDateTime.plusHours(8).toLocalDate();
		LocalDate endTime = endDateTime.plusHours(8).toLocalDate();

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
		LocalDate now = pttHistory.get(0).getTime();
		List<String> xAxisData = new ArrayList<>();
		List<Double> pttData = new ArrayList<>();
		List<Double> b30Data = new ArrayList<>();
		List<Double> r10Data = new ArrayList<>();
		if(now.isAfter(beginTime)){ //pre
			PttHistory last = pttHistoryMapper.getLatestBeforeDate(userId, beginTime);
			if(last == null){
				last = new PttHistory(userId, 0.0, 0.0, 0.0, null);
			}
			while(beginTime.isBefore(now)){
				String date = beginTime.format(DateTimeFormatter.ISO_DATE);
				xAxisData.add(date);
				pttData.add(last.getPtt());
				b30Data.add(last.getB30());
				r10Data.add(last.getR10());
				beginTime = beginTime.plusDays(interval);
			}
		}
		for(int pointer = 0; ; ){ //middle
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
		if(now.isBefore(endTime)){ //post
			now = now.plusDays(interval);
			PttHistory lastHistory = pttHistory.get(pttHistory.size() - 1);
			while(now.isBefore(endTime) && !now.isAfter(LocalDate.now())){
				String date = now.format(DateTimeFormatter.ISO_DATE);
				xAxisData.add(date);
				pttData.add(lastHistory.getPtt());
				b30Data.add(lastHistory.getB30());
				r10Data.add(lastHistory.getR10());
				now = now.plusDays(interval);
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
