package com.aptc.configuration;

import com.aptc.exception.*;
import com.aptc.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

	@ExceptionHandler(DataException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result<String> handleDataException(Exception ex){
		return Result.error(DataException.code, DataException.friendlyMsg);
	}

	@ExceptionHandler(DataProcessingException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Result<String> handleDataProcessingException(Exception ex){
		return Result.error(DataProcessingException.code, DataProcessingException.friendlyMsg);
	}

	@ExceptionHandler(FileIOException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Result<String> handleFileIOException(Exception ex){
		return Result.error(FileIOException.code, FileIOException.friendlyMsg);
	}

	@ExceptionHandler(UserAuthException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Result<String> handleUserAuthException(Exception ex){
		return Result.error(UserAuthException.code, UserAuthException.friendlyMsg);
	}

	@ExceptionHandler(UserStatusException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Result<String> handleUserStatusException(Exception ex){
		return Result.error(UserStatusException.code, UserStatusException.friendlyMsg);
	}

}
