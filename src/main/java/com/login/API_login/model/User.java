package com.login.API_login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {
    int id;
    String email;
    String password;
    int id_role;
}
