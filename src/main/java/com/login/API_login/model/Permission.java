package com.login.API_login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe responsável por abstrair uma linha da tabela permissions.
 */
@Getter
@Setter
@AllArgsConstructor
public class Permission {
    int id;
    String permission;
}
