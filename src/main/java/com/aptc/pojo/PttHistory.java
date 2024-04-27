package com.aptc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PttHistory {
	private Integer uid;
	private Double ptt;
	private Double b30;
	private Double r10;
	private LocalDate time;
}
