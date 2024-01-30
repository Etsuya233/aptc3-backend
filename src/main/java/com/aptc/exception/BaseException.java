package com.aptc.exception;

public class BaseException extends Exception{
	public static final Integer code = 0;
	public static final String friendlyMsg = "其他异常！";

	public BaseException(){}

	public BaseException(String msg){
		super(msg);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
