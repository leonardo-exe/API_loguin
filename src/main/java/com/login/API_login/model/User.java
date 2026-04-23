package com.login.API_login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe responsável por abstrair uma linha da tabela users.
 */
@Getter
@Setter
@AllArgsConstructor
public class User {
    int id;
    String email;
    String password;
    int id_role;
}
