package com.login.API_login.service;

import java.sql.SQLException;
import com.login.API_login.dao.*;
import com.login.API_login.dto.*;
import com.login.API_login.exception.*;
import com.login.API_login.model.*;
import com.login.API_login.util.Validator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
	@Autowired
	private DaoUser daoU;
	@Autowired
	private DaoRole daoR;
	private boolean permissionExist(int id_role, String permission) throws SQLException {
		List<Permission> list = daoR.queryPermissionForRole(id_role);
		for (Permission i : list)
			if (i.getPermission().equals(permission))
				return true;
		return false;
	}
	public LoginResponseDTO register(LoginInputDTO value) {
		try {
			if (!Validator.validateEmail(value.getEmail()))
				throw new InvalidEmailException("email invalido");
			if (daoU.queryId(new User(-1, value.getEmail(), value.getPassword(), -1)) != -1)
				throw new RegisteredUserException("ja existe cadastro nesse email");
			if (Validator.validatePassword(value.getPassword())) {
				String hash = BCrypt.hashpw(value.getPassword(), BCrypt.gensalt());
				User user = new User(-1, value.getEmail(), hash, 1);
				daoU.insert(user);
				return new LoginResponseDTO(user.getEmail(), "User");
			}
			return null;
		}
		catch (SQLException e) {
			throw new RegisteredUserException("erro interno ao acessar o banco de dados");
		}
	}
	public LoginResponseDTO defineRole(LoginResponseDTO request, LoginResponseDTO value, String role) {
		try {	
			User user1 = new User(-1, request.getEmail(), null, -1),
				 user2 = new User(-1, value.getEmail(), null, -1);		
			if (daoU.queryId(user2) == -1)
				throw new RegisteredUserException("usuario nao encontrado");
			user1 = daoU.query(daoU.queryId(user1));
			user2 = daoU.query(daoU.queryId(user2));
			LoginResponseDTO response = new LoginResponseDTO(value.getEmail(), daoR.queryRoleForId(user2.getId_role()));
			int idR = daoR.queryId(new Role(-1, role));
			String roleRequest = daoR.queryRoleForId(user1.getId_role());
			String roleValue = daoR.queryRoleForId(user2.getId_role());
			boolean autorizado = false;
			if (role.equals("Admin")) 
				autorizado = permissionExist(user1.getId_role(), "role:admin");
			else if (role.equals("Moderador") || role.equals("User")) 
				autorizado = permissionExist(user1.getId_role(), "role:moderador") && !roleValue.equals("Admin");
			if (autorizado) {
				daoU.updateRole(user2, idR);
				response = new LoginResponseDTO(user2.getEmail(), role);
			}
			else 
				throw new NotAllowedException(roleRequest + " nao tem permissao para definir " + role);
			return response;
		}
		catch (SQLException e) {
			throw new RegisteredUserException("erro interno ao acessar banco de dados");
		}
	}
	public LoginResponseDTO delete(LoginResponseDTO request, LoginResponseDTO value) {
		try {
			User user1 = daoU.query(daoU.queryId(new User(-1, request.getEmail(), null, -1))),
				 user2 = daoU.query(daoU.queryId(new User(-1, value.getEmail(), null, -1)));
			if (user1 == null || user2 == null)
				throw new RegisteredUserException("usuario nao encontrado");
			String roleRequest = daoR.queryRoleForId(user1.getId_role());
			if (!this.permissionExist(user1.getId_role(), "access:admin"))
				throw new NotAllowedException(roleRequest + " nao tem permissao para excluir usuarios");
			daoU.delete(user2);
			return new LoginResponseDTO(user1.getEmail(), roleRequest);
		}
		catch (SQLException e) {
			throw new RegisteredUserException("erro interno ao acessar banco de dados");
		}
	}
	public List<LoginResponseDTO> list(LoginResponseDTO request) {
		try {
			User user = daoU.query(daoU.queryId(new User(-1, request.getEmail(), null, -1)));
			if (user == null)
				throw new RegisteredUserException("usuario nao encontrado");
			String roleRequest = daoR.queryRoleForId(user.getId_role());
			if (!this.permissionExist(user.getId_role(), "access:all"))
				throw new NotAllowedException(roleRequest + " nao tem permissao para consultar usuarios");
			List<LoginResponseDTO> list = new ArrayList<>();
			for (User i : daoU.queryAll())
				list.add(new LoginResponseDTO(i.getEmail(), daoR.queryRoleForId(i.getId_role())));
			return list;
		}
		catch (SQLException e) {
			throw new RegisteredUserException("erro interno ao acessar banco de dados");
		}
	}
}
