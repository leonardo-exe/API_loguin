package com.login.API_login.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    int id;
    String email;
    String password;
    int id_role;
}
