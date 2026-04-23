package com.login.API_login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Estrutura que representa exceções para o usuário.
 */
@Getter
@Setter
@AllArgsConstructor
public class ApiErrorDTO {
    /**
     * Hora exata em que a exceção ocorreu.
     */
    String date;
    /**
     * Código HTTP retornado.
     */
    int status;
    /**
     * Nome da exceção.
     */
    String error;
    /**
     * Mensagem registrada na exceção.
     */
    String message;
}
