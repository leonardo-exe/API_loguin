package com.login.API_login.service;

import com.login.API_login.dao.*;
import com.login.API_login.dto.*;
import com.login.API_login.exception.*;
import com.login.API_login.model.User;
import com.login.API_login.util.Validator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.sql.SQLException;
import java.util.Date;

@Service
public class AuthService {
	private final String ASSINATURA = System.getenv("ASSINATURA");
	private final SecretKey KEY = Keys.hmacShaKeyFor(ASSINATURA.getBytes());
	@Autowired
	private DaoUser daoU;
	@Autowired
	private DaoRole daoR;
	private final int EXPIRE_IN_MIN = 60; 
	private String gerarToken(String username, Date exp) {
		return Jwts.builder().
			    subject(username).
				issuedAt(new Date()).
				expiration(exp).
				signWith(KEY).compact();
	}
	public TokenResponseDTO login(LoginInputDTO value) {
		try {
			if (!Validator.validateEmail(value.getEmail()))
				throw new InvalidEmailException("email invalido");
			Validator.validatePassword(value.getPassword());
			User user = new User(-1, value.getEmail(), null, -1);
			int id = daoU.queryId(user);
			if (id == -1)
				throw new RegisteredUserException("usuario nao cadastrado");
			user = daoU.query(id);
			if (!Validator.validatePasswordHash(value.getPassword(), user.getPassword()))
				throw new InvalidPasswordException("senha incorreta");
			Date exp = new Date(System.currentTimeMillis() + 1000 * 60 * EXPIRE_IN_MIN);
			return new TokenResponseDTO(gerarToken(user.getEmail(), exp), daoR.queryRoleForId(user.getId_role()), exp.toString());
		}
		catch (SQLException e) {
			throw new RegisteredUserException("erro interno ao acessar o banco de dados");
		}
	}
	public LoginResponseDTO login(TokenResponseDTO token) {
		try {
			Claims claims = Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token.getToken()).getPayload();
			return new LoginResponseDTO(claims.getSubject(), token.getRole());
		}
		catch (JwtException e) {
			throw new InvalidTokenException(e.getMessage());
		}
	}
}
