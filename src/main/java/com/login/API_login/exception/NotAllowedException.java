package com.login.API_login.exception;

import lombok.Getter;
import java.util.Date;

@Getter
public class NotAllowedException extends RuntimeException {
	Date date = new Date(System.currentTimeMillis());
	public NotAllowedException(String message) {
		super(message);
	}
}
