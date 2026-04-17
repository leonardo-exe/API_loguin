package com.login.API_login.config;

import java.sql.*;

public abstract class Access {
	private static final String user = System.getenv("DB_USER");
	private static final String password = System.getenv("DB_PASSWORD");
	private static final String url =  System.getenv("DB_URL") + "login";
	public static Connection getAccess() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
}
