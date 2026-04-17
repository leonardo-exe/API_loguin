package com.login.API_login.service;

import com.login.API_login.exception.InvalidEmailException;
import com.login.API_login.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMail;
    @Value("${spring.mail.username}")
    private String email;
    public void toSendEmail(String destiny, String text) {
        if (!Validator.validateEmail(destiny))
            throw new InvalidEmailException("email invalido");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(destiny);
        message.setSubject("Codigo de recuperacao");
        message.setText(text);
        javaMail.send(message);
    }
    public String modelEmail(String cod) {
        return "Codigo de autenticacao com validade de 15 minutos " + cod;
    }
}
