package com.login.API_login.exception;

import lombok.Getter;
import java.util.Date;

@Getter
public class RegisteredUserException extends RuntimeException {
	Date date = new Date(System.currentTimeMillis());
	public RegisteredUserException(String message) {
		super(message);
	}
}
