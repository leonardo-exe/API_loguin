package com.login.API_login.util;

import org.mindrot.jbcrypt.BCrypt;
import org.apache.commons.validator.routines.EmailValidator;
import com.login.API_login.exception.*;

public abstract class Validator {
	private static final String ALLOWED_ESPECIAL_CHARS = "!@#$%&*_-.";
	public static boolean validatePasswordHash(String password, String passwordHash) {
		return BCrypt.checkpw(password, passwordHash);
	}
	public static boolean validatePassword(String password) {
		if (password == null || password.length() < 6)
			throw new InvalidPasswordException("deve possuir 6 caracteres");
		for (char c : password.toCharArray()) 
			if (!Character.isLetterOrDigit(c) && !(ALLOWED_ESPECIAL_CHARS.indexOf(c) >= 0))
				throw new InvalidPasswordException("caractere '" + c + "' nao permitido");
		return true;
	}
	public static boolean validateEmail(String email) {
		return EmailValidator.getInstance().isValid(email);
	}
}
