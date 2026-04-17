package com.login.API_login.dao;

import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {
	void insert(T value) throws SQLException;
	void delete(T value) throws SQLException;
	T query(int id) throws SQLException;
	int queryId(T value) throws SQLException;
	List<T> queryAll() throws SQLException;
}
