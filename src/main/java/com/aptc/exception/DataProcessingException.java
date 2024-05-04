package com.aptc.exception;

public class DataProcessingException extends BaseRuntimeException {
	public static final Integer code = 2;
	public static final String friendlyMsg = "数据处理出现异常！";

	public DataProcessingException() {

	}

	public DataProcessingException(String msg) {
		super(msg);
	}

	public DataProcessingException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataProcessingException(Throwable cause) {
		super(cause);
	}

	public DataProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
