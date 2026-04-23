package com.login.API_login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Estrutura de retorno que representa o usuário.
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
	String email;
	String role;
}
