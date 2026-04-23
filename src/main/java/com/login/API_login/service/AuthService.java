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
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.sql.SQLException;
import java.util.Date;

/**
 * Classe de serviço responsável por realizar o login do usuário
 */
@Service
public class AuthService {
	/**
	 * String assinatura presente nos tokens
	 */
	@Value("${app.token.signature}")
	private String SIGNATURE;
	private SecretKey KEY;
	@Autowired
	private DaoUser daoU;
	@Autowired
	private DaoRole daoR;
	/**
	 * Define em quanto tempo o token de login expira.
	 */
	private final int EXPIRE_IN_MIN = 60 * 24;
	@PostConstruct
	private void init() {
		this.KEY = Keys.hmacShaKeyFor(SIGNATURE.getBytes());
	}

	/**
	 * Gera um token temporário.
	 * @param username Email do usuário.
	 * @param exp Date contendo a validade do token.
	 * @return Token no formato String.
	 */
	public String gerarToken(String username, Date exp) {
		return Jwts.builder().
			    subject(username).
				issuedAt(new Date()).
				expiration(exp).
				signWith(KEY).compact();
	}

	/**
	 * Conecta o usuário ao sistema através de seus dados de login (email e senha) e gera um token de acesso temporário.
	 * @param value Classe DTO contendo email e senha.
	 * @return Classe DTO com o token temporário gerado, a role do usuário e o momento em que o token expira.
	 * @throws RegisteredUserException Usuário não encontrado.
	 */
	public TokenResponseDTO login(LoginInputDTO value) throws RegisteredUserException {
		try {
			if (!Validator.validateEmail(value.getEmail()))
				throw new InvalidEmailException("invalid email");
			Validator.validatePassword(value.getPassword());
			User user = new User(-1, value.getEmail(), null, -1);
			int id = daoU.queryId(user);
			if (id == -1)
				throw new RegisteredUserException("unregistered user");
			user = daoU.query(id);
			if (!Validator.validatePasswordHash(value.getPassword(), user.getPassword()))
				throw new InvalidPasswordException("invalid password");
			Date exp = new Date(System.currentTimeMillis() + 1000 * 60 * EXPIRE_IN_MIN);
			return new TokenResponseDTO(gerarToken(user.getEmail(), exp), daoR.queryRoleForId(user.getId_role()), exp.toString());
		}
		catch (SQLException e) {
			throw new RegisteredUserException("internal error accessing database");
		}
	}

	/**
	 * Conecta o usuário ao sistema através do token de acesso temporário.
	 * @param token Classe DTO contendo o token.
	 * @return Classe DTO contendo email e role do usuário.
	 * @throws InvalidTokenException Token expirado ou inválido.
	 */
	public LoginResponseDTO login(TokenResponseDTO token) throws InvalidTokenException {
		try {
			Claims claims = Jwts.parser().verifyWith(KEY).build().parseSignedClaims(token.getToken()).getPayload();
			return new LoginResponseDTO(claims.getSubject(), token.getRole());
		}
		catch (JwtException e) {
			throw new InvalidTokenException("expired token");
		}
	}
}
