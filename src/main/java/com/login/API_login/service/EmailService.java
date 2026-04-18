package com.login.API_login.service;

import com.login.API_login.dao.DaoUser;
import com.login.API_login.exception.InvalidEmailException;
import com.login.API_login.exception.RegisteredUserException;
import com.login.API_login.model.User;
import com.login.API_login.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.sql.SQLException;
import java.util.Date;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMail;
    private final Random random = new Random();
    @Autowired
    private DaoUser daoU;
    @Autowired
    private AuthService authService;
    @Value("${spring.mail.username}")
    private String email;
    private static final String namePlatform = "API login";
    private final int EXPIRE_IN_MIN = 15;
    public void toSendEmail(String destiny, String text) {
        if (!Validator.validateEmail(destiny))
            throw new InvalidEmailException("invalid email");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(destiny);
        message.setSubject("[" + namePlatform + "] authentication cod");
        message.setText(text);
        javaMail.send(message);
    }
    private String cod() {
        String result = "";
        for (int i = 0; i < 6; i++)
            result += random.nextInt(10);
        return result;
    }
    private String modelEmail(String cod) {
        return "Here is authentication code:\n\n" + cod +
                "\n\nThis code is valid for " + EXPIRE_IN_MIN +
                " minutes and can only be used once.\n\n" +
                "Thanks,\n" + namePlatform;
    }
    public void recoverPassword(String email) {
        try {
            User user = daoU.query(daoU.queryId(new User(-1, email, null, -1)));
            if (user == null)
                throw new RegisteredUserException("unregistered user");
            String cod = this.cod();
            String message = this.modelEmail(cod);
            String token = authService.gerarToken(cod, new Date(System.currentTimeMillis() + 1000 * 60 * EXPIRE_IN_MIN));
            user.setPassword(token);
            daoU.recoverToken(user);
            this.toSendEmail(email, message);
        }
        catch (SQLException e) {
            throw new RegisteredUserException("internal error accessing database");
        }
    }
}

