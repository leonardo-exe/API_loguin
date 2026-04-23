package com.login.API_login.exception;

import lombok.Getter;
import java.util.Date;

/**
 * Exceção lançada quando um email possui um formato inválido.
 */
@Getter
public class InvalidEmailException extends RuntimeException {
	/**
	 * Data e hora exata em que a violação de permissão ocorreu para fins de log.
	 */
	Date date = new Date(System.currentTimeMillis());
	public InvalidEmailException(String message) {
		super(message);
	}
}
