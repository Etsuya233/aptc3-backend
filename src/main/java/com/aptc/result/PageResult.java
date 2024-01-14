package com.aptc.result;

import lombok.Data;

import java.util.List;

@Data
public class PageResult {
	private Integer total;
	private List records;

}
