package com.login.API_login.controller;

import com.login.API_login.dto.*;
import com.login.API_login.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Classe de controle que recebe requisições relacionadas a login de usuários.
 */
@RestController
@RequestMapping("/login")
public class ControllerLogin {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    /**
     * Conecta o usuário utilizando seu token de acesso temporário.
     * @param value Objeto DTO contendo o token de acesso temporário.
     * @return objeto DTO contendo email e role do usuário.
     */
    @PostMapping("/token")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody TokenResponseDTO value) {
        return ResponseEntity.ok(authService.login(value));
    }

    /**
     * Conecta o usuário utilizando seus dados de login (email e senha)
     * @param value Objeto DTO contendo email e senha.
     * @return Objeto DTO contendo token de acesso temporário gerado.
     */
    @PostMapping("/user")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginInputDTO value) {
        return ResponseEntity.ok(authService.login(value));
    }

    /**
     * Registra um novo usuário no sistema.
     * @param value Objeto DTO contendo email e senha.
     * @return Objeto DTO contendo token de acesso temporário gerado.
     */
    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@RequestBody LoginInputDTO value) {
        userService.register(value);
        return ResponseEntity.ok(authService.login(value));
    }

    /**
     * Envia um código de autenticação por email.
     * @param value Objeto DTO contendo o email do usuário.
     */
    @PostMapping("/recover")
    public void recoverPassword(@RequestBody LoginInputDTO value) {
        emailService.recoverPassword(value.getEmail());
    }

    /**
     * Valida um código de autenticação e registra a nova senha do usuário.
     * @param value Objeto DTO contendo o código de autenticação o email e a nova senha.
     */
    @PutMapping("/recover")
    public void recoverPassword(@RequestBody CodAuthenticatorDTO value) {
        userService.newPassword(value);
    }
}
