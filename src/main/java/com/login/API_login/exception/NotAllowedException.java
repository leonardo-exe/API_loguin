package com.login.API_login.exception;

import lombok.Getter;
import java.util.Date;

/**
 * Exceção lançada quando um usuário tenta realizar uma ação da qual não possui permissão.
 * Ex: Usuário tentou editar outro usuário com cargo maior ou igual ao seu.
 * Ex: Usuário tentou visualizar dados de outros usuários sem ser moderador ou superior.
 */
@Getter
public class NotAllowedException extends RuntimeException {
	/**
	 * Data e hora exata em que a violação de permissão ocorreu para fins de log.
	 */
	Date date = new Date(System.currentTimeMillis());
	public NotAllowedException(String message) {
		super(message);
	}
}
