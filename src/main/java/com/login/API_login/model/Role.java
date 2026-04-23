package com.login.API_login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe responsável por abstrair uma linha da tabela roles.
 */
@Getter
@Setter
@AllArgsConstructor
public class Role {
    int id;
    String role;
}
