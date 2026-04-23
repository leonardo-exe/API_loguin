package com.login.API_login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Estrutura recebida ao conectar um usuário.
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginInputDTO {
	String email;
	String password;
}
