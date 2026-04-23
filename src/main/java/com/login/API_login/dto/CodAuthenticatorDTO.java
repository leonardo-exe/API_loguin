package com.login.API_login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Estrutura recebida na autenticação do código enviado pelo usuário.
 */
@Getter
@Setter
@AllArgsConstructor
public class CodAuthenticatorDTO {
    /**
     * Código de autenticação recebido no email.
     */
    String cod;
    /**
     * Estrutura com o email e nova senha.
     */
    LoginInputDTO user;
}
