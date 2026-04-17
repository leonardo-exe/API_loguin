package com.login.API_login.controller;

import com.login.API_login.dto.*;
import com.login.API_login.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class ControllerUser {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @DeleteMapping("/delete")
    public ResponseEntity<LoginResponseDTO> delete(@RequestBody UpdateInputDTO value) {
        LoginResponseDTO userResponse = authService.login(value.getRequest());
        return ResponseEntity.ok(userService.delete(userResponse, value.getValue()));
    }
    @GetMapping("/list")
    public ResponseEntity<List<LoginResponseDTO>> listAll(@RequestBody TokenResponseDTO value) {
        LoginResponseDTO userResponse = authService.login(value);
        return ResponseEntity.ok(userService.list(userResponse));
    }
    @PostMapping("/defineRole")
    public ResponseEntity<LoginResponseDTO> defineRole(@RequestBody UpdateInputDTO value) {
        LoginResponseDTO request = authService.login(value.getRequest());
        return ResponseEntity.ok(userService.defineRole(request, value.getValue(), value.getRoleRequest()));
    }
}
