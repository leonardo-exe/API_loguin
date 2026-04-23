package com.login.API_login.dao;

import com.login.API_login.config.Access;
import com.login.API_login.model.*;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @implNote Dao.
 * Classe DAO (Data Access Object) responsável por todas as operações de manipulação da tabela roles.
 */
@Repository
public class DaoRole implements Dao<Role> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert(Role value) throws SQLException {
		try (
            Connection conn = Access.getAccess();
            PreparedStatement pstm = conn.prepareStatement("""
			insert into roles (role)
			values (?)
			""")
		) {
			pstm.setString(1, value.getRole());
			pstm.execute();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(Role value) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("delete from roles where id = ? and role = ?")
		) {
			pstm.setInt(1, value.getId());
			pstm.setString(2, value.getRole());
			pstm.execute();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Role query(int id) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("select * from roles where id = ?")
		) {
			pstm.setInt(1, id);
			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next())
					return new Role(rs.getInt("id"), rs.getString("role"));
				return null;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int queryId(Role value) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("select id from roles where role = ?")
		) {
			pstm.setString(1, value.getRole());
			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next())
					return rs.getInt("id");
				return -1;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Role> queryAll() throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("select * from roles");
			ResultSet rs = pstm.executeQuery()
		) {
			List<Role> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Role(rs.getInt("id"), rs.getString("role")));
			}
			return list;
		}
	}

	/**
	 * Função que busca todas as permissions disponíveis para a role especificada.
	 * @param id Chave primária da role na tabela.
	 * @return List de elementos Permission contendo as permissions da role especificada.
	 * @throws SQLException Erro ao acessar o banco de dados.
	 */
	public List<Permission> queryPermissionForRole(int id) throws SQLException {
		try (
				Connection conn = Access.getAccess();
				PreparedStatement pstm = conn.prepareStatement("""
				
						SELECT p.id, p.permission
				FROM permissions p
				INNER JOIN relations r
				ON p.id = r.id_permission
				WHERE r.id_role = ?
				""")
		) {
			pstm.setInt(1, id);
			try (ResultSet rs = pstm.executeQuery()) {
				List<Permission> list = new ArrayList<>();
				while (rs.next()) {
					list.add(new Permission(rs.getInt("id"), rs.getString("permission")));
				}
				return list;
			}
		}
	}

	/**
	 * Função que transforma uma chave primária de uma role em sua respectiva String.
	 * @param id Chave primária da role na tabela.
	 * @return String com o nome da role.
	 * @throws SQLException Erro ao acessar o banco de dados.
	 */
	public String queryRoleForId(int id) throws SQLException {
		try (
			Connection conn = Access.getAccess();
			PreparedStatement pstm = conn.prepareStatement("select role from roles where id = ?")
		) {
			pstm.setInt(1, id);
			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next())
					return rs.getString("role");
				return "";
			}
		}
	}
}