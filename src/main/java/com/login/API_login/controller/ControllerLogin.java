package com.login.API_login.controller;

import com.login.API_login.dto.*;
import com.login.API_login.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class ControllerLogin {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @PostMapping("/token")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody TokenResponseDTO value) {
        return ResponseEntity.ok(authService.login(value));
    }
    @PostMapping("/user")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginInputDTO value) {
        return ResponseEntity.ok(authService.login(value));
    }
    @PostMapping("/register")
    public ResponseEntity<TokenResponseDTO> register(@RequestBody LoginInputDTO value) {
        userService.register(value);
        return ResponseEntity.ok(authService.login(value));
    }
}
