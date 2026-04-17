package com.login.API_login.dao;

import com.login.API_login.config.Access;
import com.login.API_login.model.Permission;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DaoPermission implements Dao<Permission> {
	@Override
    public void insert(Permission value) throws SQLException {
		try (
                Connection conn = Access.getAccess();
                PreparedStatement pstm = conn.prepareStatement("""
				insert into permissions (permission)
				values (?)
				""")
		) {
			pstm.setString(1, value.getPermission());
			pstm.execute();
		}
	}
	@Override
	public void delete(Permission value) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("""
				delete from permissions
				where id = ? and permission = ?
				""")
		) {
			pstm.setInt(1, value.getId());
			pstm.setString(2, value.getPermission());
			pstm.execute();
		}
	}
	@Override
	public Permission query(int id) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("select * from permissions where id = ?")
		) {
			pstm.setInt(1, id);
			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next())
					return new Permission(rs.getInt("id"), rs.getString("permission"));
				return null;
			}
		}
	}
	@Override
	public int queryId(Permission value) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("select id from permissions where permission = ?")
		) {
			pstm.setString(1, value.getPermission());
			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next())
					return rs.getInt("id");
				return -1;
			}
		}
	}
	@Override
	public List<Permission> queryAll() throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("select * from permissions");
			ResultSet rs = pstm.executeQuery()
		) {
			List<Permission> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Permission(rs.getInt("id"), rs.getString("permission")));
			}
			return list;
		}
	}
}
