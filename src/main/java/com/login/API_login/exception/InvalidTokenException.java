package com.login.API_login.exception;

import lombok.Getter;
import java.util.Date;

/**
 * Exceção lançada quando um token temporário foi expirado.
 */
@Getter
public class InvalidTokenException extends RuntimeException {
	/**
	 * Data e hora exata em que a violação de permissão ocorreu para fins de log.
	 */
	Date date = new Date(System.currentTimeMillis());
	public InvalidTokenException(String message) {
		super(message);
	}
}
