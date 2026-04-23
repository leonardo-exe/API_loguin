package com.login.API_login.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface com as operações básicas que as classes que acessam o banco de dados devem implementar
 * @param <T>
 */
public interface Dao<T> {
	/**
	 * Insere uma linha na tabela correspondente.
	 * @param value Classe que abstrai uma linha da tabela.
	 * @throws SQLException Erro ao acessar o banco de dados.
	 */
	void insert(T value) throws SQLException;
	/**
	 * Deleta a linha de sua da tabela correspondente.
	 * @param value Classe que abstrai uma linha da tabela.
	 * @throws SQLException Erro ao acessar o banco de dados.
	 */
	void delete(T value) throws SQLException;
	/**
	 * Busca uma linha utilizando sua chave primária.
	 * @param id Chave primária do elemento na tabela.
	 * @return Classe que abstrai a linha na tabela ou null.
	 * @throws SQLException Erro ao acessar o banco de dados.
	 */
	T query(int id) throws SQLException;
	/**
	 * Busca uma chave primária utilizando uma classe que abstrai a linha na tabela.
	 * @param value Classe que abstrai a linha na tabela.
	 * @return Chave primária do elemento na tabela ou -1.
	 * @throws SQLException Erro ao acessar o banco de dados.
	 */
	int queryId(T value) throws SQLException;
	/**
	 * Busca todos os elementos na tabela.
	 * @return List de classes que abstraem linhas, contém todos os elementos da tabela.
	 * @throws SQLException Erro ao acessar o banco de dados.
	 */
	List<T> queryAll() throws SQLException;
}
