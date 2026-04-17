package com.login.API_login.controller;

import com.login.API_login.dto.ApiErrorDTO;
import com.login.API_login.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiErrorDTO> handleTokenException(InvalidTokenException exc) {
        ApiErrorDTO err = new ApiErrorDTO(exc.getDate().toString(), HttpStatus.UNAUTHORIZED.value(), "UNAUTHORIZED", exc.getMessage());
        System.out.println(exc.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(err);
    }
    @ExceptionHandler(NotAllowedException.class)
    public ResponseEntity<ApiErrorDTO> handlePermissionException(NotAllowedException exc) {
        ApiErrorDTO err = new ApiErrorDTO(exc.getDate().toString(), HttpStatus.FORBIDDEN.value(), "FORBIDDEN", exc.getMessage());
        System.out.println(exc.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(err);
    }
    @ExceptionHandler({InvalidEmailException.class, InvalidPasswordException.class})
    public ResponseEntity<ApiErrorDTO> handleValidatorException(RuntimeException exc) {
        ApiErrorDTO err = new ApiErrorDTO((new Date(System.currentTimeMillis())).toString(), HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", exc.getMessage());
        System.out.println(exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(err);
    }
    @ExceptionHandler(RegisteredUserException.class)
    public ResponseEntity<ApiErrorDTO> handleUserException(RegisteredUserException exc) {
        ApiErrorDTO err = new ApiErrorDTO(exc.getDate().toString(), HttpStatus.NOT_ACCEPTABLE.value(), "NOT_ACCEPTABLE", exc.getMessage());
        System.out.println(exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE.value()).body(err);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleServidorException(Exception exc) {
        ApiErrorDTO err = new ApiErrorDTO((new Date(System.currentTimeMillis())).toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR", exc.getMessage());
        System.out.println(exc.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(err);
    }
}
