package com.login.API_login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Estrutura recebida quando um usuário requere uma atualização em outro usuário.
 */
@Getter
@Setter
@AllArgsConstructor
public class UpdateInputDTO {
    /**
     * Estrutura com token do usuário com permissão para editar.
     */
    TokenResponseDTO request;
    /**
     * Estrutura com email e cargo atual do usuário a ser alterado.
     */
    LoginResponseDTO value;
    /**
     * Role desejada.
     */
    String roleRequest;
}
