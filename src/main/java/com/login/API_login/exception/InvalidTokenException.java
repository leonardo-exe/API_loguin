package com.login.API_login.exception;

import lombok.Getter;
import java.util.Date;

@Getter
public class InvalidTokenException extends RuntimeException {
	Date date = new Date(System.currentTimeMillis());
	public InvalidTokenException(String message) {
		super(message);
	}
}
