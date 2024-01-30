package com.aptc.exception;

public class UserAuthException extends BaseException{
	public static final Integer code = 4;
	public static final String friendlyMsg = "用户信息不匹配！";

	public UserAuthException() {

	}

	public UserAuthException(String msg) {
		super(msg);
	}

	public UserAuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAuthException(Throwable cause) {
		super(cause);
	}

	public UserAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
