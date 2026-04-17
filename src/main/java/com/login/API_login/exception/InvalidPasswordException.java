package com.login.API_login.exception;

import lombok.Getter;
import java.util.Date;

@Getter
public class InvalidPasswordException extends RuntimeException {
	Date date = new Date(System.currentTimeMillis());
	public InvalidPasswordException(String message) {
		super(message);
	}
}
