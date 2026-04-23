package com.login.API_login.util;

import org.mindrot.jbcrypt.BCrypt;
import org.apache.commons.validator.routines.EmailValidator;
import com.login.API_login.exception.*;

/**
 * Classe de apoio responsável por fazer validações de dados.
 */
public abstract class Validator {

	/**
	 * Caracteres especiais permitidos em uma senha.
	 */
	private static final String ALLOWED_ESPECIAL_CHARS = "!@#$%&*_-.";

	/**
	 * Função que verifica se uma senha é o equivalente a um hash criado pela classe BCrypt.
	 * @param password Senha a ser testada.
	 * @param passwordHash Senha em formato hash.
	 * @return Valor booleano representando a corretude da senha.
	 */
	public static boolean validatePasswordHash(String password, String passwordHash) {
		return BCrypt.checkpw(password, passwordHash);
	}

	/**
	 * Função que verifica se uma senha possui o formato permitido
	 * Formato: 6 ou mais caracteres, letras, dígitos e caracteres especiais definidos em ALLOWED_ESPECIAL_CHARS.
	 * @param password Senha a ser analisada.
	 * @return Valor booleano.
	 * @throws InvalidPasswordException Possui menos de 6 caracteres ou caractere inválido.
	 */
	public static boolean validatePassword(String password) throws InvalidPasswordException {
		if (password == null || password.length() < 6)
			throw new InvalidPasswordException("deve possuir 6 caracteres");
		for (char c : password.toCharArray()) 
			if (!Character.isLetterOrDigit(c) && !(ALLOWED_ESPECIAL_CHARS.indexOf(c) >= 0))
				throw new InvalidPasswordException("caractere '" + c + "' nao permitido");
		return true;
	}

	/**
	 * Função que verifica se um email está no padrão correto.
	 * @param email Email a ser analisado.
	 * @return Valor booleano.
	 */
	public static boolean validateEmail(String email) {
		return EmailValidator.getInstance().isValid(email);
	}
}
