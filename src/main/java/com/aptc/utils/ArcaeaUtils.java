package com.aptc.utils;

import org.springframework.context.annotation.Bean;

public class ArcaeaUtils {
	public static double pptCalc(int score, double difficulty){
		if(score >= 10000000) return difficulty + 2;
		else if(score >= 9800000) return difficulty + 1 + 1.0 * (score - 9800000) / 200000;
		double ptt = difficulty + 1.0 * (score - 9500000) / 300000;
		return (ptt >= 0)? ptt: 0;
	}
}
