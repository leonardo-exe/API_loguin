package com.login.API_login.controller;

import com.login.API_login.dto.*;
import com.login.API_login.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Classe de controle que recebe requisições relacionadas a manipulação de dados do usuário.
 */
@RestController
@RequestMapping("/user")
public class ControllerUser {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    /**
     * Deleta um usuário do sistema caso o requerente possua permissão necessária.
     * @param value Objeto DTO contendo o token de acesso de um usuário já conectado e o email do usuário a ser deletado.
     * @return Objeto DTO com email e role do requerente.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<LoginResponseDTO> delete(@RequestBody UpdateInputDTO value) {
        LoginResponseDTO userResponse = authService.login(value.getRequest());
        return ResponseEntity.ok(userService.delete(userResponse, value.getValue()));
    }

    /**
     * Busca todos os usuários do sistema caso o requerente possua permissão necessária.
     * @param value Objeto DTO contendo o token de acesso de um usuário já conectado.
     * @return Objeto DTO contendo um vetor de objetos com email e nível de cada usuário.
     */
    @GetMapping("/list")
    public ResponseEntity<List<LoginResponseDTO>> listAll(@RequestBody TokenResponseDTO value) {
        LoginResponseDTO userResponse = authService.login(value);
        return ResponseEntity.ok(userService.list(userResponse));
    }

    /**
     * Define uma permissão para outro usuário caso o requerente possua permissão necessária.
     * @param value Objeto DTO contendo o token de acesso de um usuário já logado, o email do usuário a ser alterado e o nível de permissão desejado.
     * @return Objeto DTO contendo email e role do usuário alterado.
     */
    @PutMapping("/define-role")
    public ResponseEntity<LoginResponseDTO> defineRole(@RequestBody UpdateInputDTO value) {
        LoginResponseDTO request = authService.login(value.getRequest());
        return ResponseEntity.ok(userService.defineRole(request, value.getValue(), value.getRoleRequest()));
    }

    /**
     * Registra uma nova senha para o usuário caso os dados de login estejam corretos.
     * @param value Objeto DTO contendo a nova senha, o email do usuário e a senha antiga.
     * @return objeto DTO contendo email e role.
     */
    @PutMapping("/new-password")
    public ResponseEntity<LoginResponseDTO> newPassword(@RequestBody NewPasswordDTO value) {
        authService.login(value.getUser());
        LoginInputDTO user = value.getUser();
        user.setPassword(value.getPassword());
        return ResponseEntity.ok(userService.newPassword(user));
    }
}
