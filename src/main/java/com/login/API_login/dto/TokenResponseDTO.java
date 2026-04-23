package com.login.API_login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Estrutura que retorna o token de acesso temporário para o usuário.
 */
@Getter
@Setter
@AllArgsConstructor
public class TokenResponseDTO {
	String token;
	String role;
	String expireIn;
}
