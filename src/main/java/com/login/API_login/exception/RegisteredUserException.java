package com.login.API_login.exception;

import lombok.Getter;
import java.util.Date;

/**
 * Exceção lançada quando é requisitado atualização de um usuário não existente.
 */
@Getter
public class RegisteredUserException extends RuntimeException {
	/**
	 * Data e hora exata em que a violação de permissão ocorreu para fins de log.
	 */
	Date date = new Date(System.currentTimeMillis());
	public RegisteredUserException(String message) {
		super(message);
	}
}
