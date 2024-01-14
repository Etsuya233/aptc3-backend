package com.aptc.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

	private Integer code; //200
	private String msg;
	private T data;

	public static <T> Result<T> success(T data){
		Result<T> result = new Result<>();
		result.data = data;
		result.code = 200;
		return result;
	}

	public static <T> Result<T> success(){
		Result<T> result = new Result<>();
		result.code = 200;
		return result;
	}

	public static <T> Result<T> error(String msg){
		Result<T> result = new Result<>();
		result.msg = msg;
		return result;
	}

}
