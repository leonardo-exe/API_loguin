package com.login.API_login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateInputDTO {
    TokenResponseDTO request;
    LoginResponseDTO value;
}
