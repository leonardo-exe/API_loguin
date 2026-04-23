package com.login.API_login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Estrutura recebida ao atualizar a senha de um usuário existente.
 */
@Getter
@Setter
@AllArgsConstructor
public class NewPasswordDTO {
    /**
     * Nova senha.
     */
    String password;
    /**
     * Estrutura com o email e antiga senha.
     */
    LoginInputDTO user;
}
