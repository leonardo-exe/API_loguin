package com.login.API_login.exception;

import lombok.Getter;
import java.util.Date;

@Getter
public class InvalidEmailException extends RuntimeException {
	Date date = new Date(System.currentTimeMillis());
	public InvalidEmailException(String message) {
		super(message);
	}
}
