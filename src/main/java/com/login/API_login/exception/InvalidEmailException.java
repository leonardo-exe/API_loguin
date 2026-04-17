package com.login.API_login.exception;

public class InvalidEmailException extends RuntimeException {
	public InvalidEmailException(String message) {
		super(message);
	}
}
