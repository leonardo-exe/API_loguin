package com.login.API_login.exception;

import lombok.Getter;
import java.util.Date;

/**
 * Exceção lançada quando uma senha possui caracteres inválidos ou quando possui menos de 8 caracteres.
 */
@Getter
public class InvalidPasswordException extends RuntimeException {
	/**
	 * Data e hora exata em que a violação de permissão ocorreu para fins de log.
	 */
	Date date = new Date(System.currentTimeMillis());
	public InvalidPasswordException(String message) {
		super(message);
	}
}
