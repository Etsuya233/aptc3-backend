package com.aptc.exception;

/**
 * 数据库操作的异常
 */
public class DataException extends BaseException{
	public static final Integer code = 1;
	public static final String friendlyMsg = "数据异常！";

	public DataException() {

	}

	public DataException(String msg) {
		super(msg);
	}

	public DataException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataException(Throwable cause) {
		super(cause);
	}

	public DataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
