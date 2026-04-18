package com.login.API_login.dao;

import com.login.API_login.config.Access;
import com.login.API_login.model.User;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

@Repository
public class DaoUser implements Dao<User> {
	@Override
	public void insert(User value) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("""
			insert into users (email, password, id_role)
			values (?, ?, ?)
			""")
		) {
			pstm.setString(1, value.getEmail());
			pstm.setString(2, value.getPassword());
			pstm.setInt(3, value.getId_role());
			pstm.execute();
		}
	}
	@Override
	public void delete(User value) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("delete from users where id = ? and email = ?")
		) {
			pstm.setInt(1, value.getId());
			pstm.setString(2, value.getEmail());
			pstm.execute();
		}
	}
	public void updateRole(User value, int id_role) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("update users set id_role = ? where id = ?")
		) {
			pstm.setInt(1, id_role);
			pstm.setInt(2, value.getId());
			pstm.execute();
		}
	}
	public void updatePassword(User value) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("update users set password = ? where id = ?")
		) {
			pstm.setString(1, value.getPassword());
			pstm.setInt(2, value.getId());
			pstm.execute();
		}
	}
	public void recoverToken(User value) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("update users set recover_token = ? where email = ?")
		) {
			pstm.setString(1, value.getPassword());
			pstm.setString(2, value.getEmail());
			pstm.execute();
		}
	}
	public void deleteToken(User value) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("delete ")
		) {

		}
	}
	public String getToken(User value) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("select recover_token from users where id = ?")
		) {
			pstm.setInt(1, value.getId());
			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next())
					return rs.getString("recover_token");
				return "";
			}
		}
	}
	@Override
	public User query(int id) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("select * from users where id = ?")
		) {
			pstm.setInt(1, id);
			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next())
					return new User(rs.getInt("id"), rs.getString("email"), rs.getString("password"), rs.getInt("id_role"));
				return null;
			}
		}
	}
	@Override
	public int queryId(User value) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("select id from users where email = ?")
		) {
			pstm.setString(1, value.getEmail());
			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next())
					return rs.getInt("id");
				return -1;
			}
		}
	}
	@Override
	public List<User> queryAll() throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("select * from users");
			ResultSet rs = pstm.executeQuery()
		) {
			List<User> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new User(rs.getInt("id"), rs.getString("email"), rs.getString("password"), rs.getInt("id_role")));
			}
			return list;
		}
	}
}
