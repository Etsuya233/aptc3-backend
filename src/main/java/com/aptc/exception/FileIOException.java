package com.aptc.exception;

/**
 * 文件的创建或者复制等出现的错误
 */
public class FileIOException extends BaseRuntimeException {
	public static final Integer code = 3;
	public static final String friendlyMsg = "系统内部异常！";

	public FileIOException() {

	}

	public FileIOException(String message) {
		super(message);
	}

	public FileIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileIOException(Throwable cause) {
		super(cause);
	}

	public FileIOException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
