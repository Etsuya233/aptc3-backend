package com.aptc.exception;

public class UserStatusException extends BaseRuntimeException {
	public static final Integer code = 5;
	public static final String friendlyMsg = "用户状态异常！";

	public UserStatusException() {

	}

	public UserStatusException(String msg) {
		super(msg);
	}

	public UserStatusException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserStatusException(Throwable cause) {
		super(cause);
	}

	public UserStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
