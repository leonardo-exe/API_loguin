package com.login.API_login.config;

import java.sql.*;

/**
 * Classe abstrata que configura o acesso ao banco de dados, utiliza variáveis de ambiente.
 */
public abstract class Access {

	/**
	 * Variável de ambiente que contém o nome de usuário para acesso ao banco.
	 */
	private static final String user = System.getenv("DB_USER");

	/**
	 * Variável de ambiente que contém a senha do usuário usado para acessar o banco.
	 */
	private static final String password = System.getenv("DB_PASSWORD");

	/**
	 * Variável de ambiente que contém a url que representa o banco de dados.
	 */
	private static final String url =  System.getenv("DB_URL") + "login";

	/**
	 * @return Connction que representa a conexão com o banco de dados.
	 * @throws SQLException Erro ao acessar o banco de dados.
	 */
	public static Connection getAccess() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
}
