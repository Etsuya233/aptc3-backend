package com.aptc.controller.user;

import com.aptc.pojo.vo.ChartVO;
import com.aptc.pojo.vo.PttChartVO;
import com.aptc.result.Result;
import com.aptc.service.PttHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/ptt")
@RequiredArgsConstructor
public class PttHistoryController {

	private final PttHistoryService pttHistoryService;

	@GetMapping("/charts")
	public Result<PttChartVO> getPttCharts(@RequestParam("beginTime") String beginTime,
										   @RequestParam("endTime") String endTime){
		return Result.success(pttHistoryService.getPttCharts(beginTime, endTime));
	}
}
