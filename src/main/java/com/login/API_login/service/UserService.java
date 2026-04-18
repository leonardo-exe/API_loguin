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
	@Autowired
	private AuthService authService;
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
				throw new InvalidEmailException("invalid email");
			if (daoU.queryId(new User(-1, value.getEmail(), value.getPassword(), -1)) != -1)
				throw new RegisteredUserException("There is already a registration with this email address.");
			Validator.validatePassword(value.getPassword());
			String hash = BCrypt.hashpw(value.getPassword(), BCrypt.gensalt());
			User user = new User(-1, value.getEmail(), hash, 1);
			daoU.insert(user);
			return new LoginResponseDTO(user.getEmail(), "User");
		}
		catch (SQLException e) {
			throw new RegisteredUserException("internal error accessing database");
		}
	}
	public LoginResponseDTO defineRole(LoginResponseDTO request, LoginResponseDTO value, String role) {
		try {	
			User user1 = new User(-1, request.getEmail(), null, -1),
				 user2 = new User(-1, value.getEmail(), null, -1);		
			if (daoU.queryId(user2) == -1)
				throw new RegisteredUserException("unregistered user");
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
				throw new NotAllowedException(roleRequest + " not allowed to define " + role);
			return response;
		}
		catch (SQLException e) {
			throw new RegisteredUserException("internal error accessing database");
		}
	}
	public LoginResponseDTO delete(LoginResponseDTO request, LoginResponseDTO value) {
		try {
			User user1 = daoU.query(daoU.queryId(new User(-1, request.getEmail(), null, -1))),
				 user2 = daoU.query(daoU.queryId(new User(-1, value.getEmail(), null, -1)));
			if (user1 == null || user2 == null)
				throw new RegisteredUserException("unregistered user");
			String roleRequest = daoR.queryRoleForId(user1.getId_role());
			if (!this.permissionExist(user1.getId_role(), "access:admin"))
				throw new NotAllowedException(roleRequest + " not allowed to delete users");
			daoU.delete(user2);
			return new LoginResponseDTO(user1.getEmail(), roleRequest);
		}
		catch (SQLException e) {
			throw new RegisteredUserException("internal error accessing database");
		}
	}
	public List<LoginResponseDTO> list(LoginResponseDTO request) {
		try {
			User user = daoU.query(daoU.queryId(new User(-1, request.getEmail(), null, -1)));
			if (user == null)
				throw new RegisteredUserException("unregistered user");
			String roleRequest = daoR.queryRoleForId(user.getId_role());
			if (!this.permissionExist(user.getId_role(), "access:all"))
				throw new NotAllowedException(roleRequest + " not allowed to query users");
			List<LoginResponseDTO> list = new ArrayList<>();
			for (User i : daoU.queryAll())
				list.add(new LoginResponseDTO(i.getEmail(), daoR.queryRoleForId(i.getId_role())));
			return list;
		}
		catch (SQLException e) {
			throw new RegisteredUserException("internal error accessing database");
		}
	}
	public LoginInputDTO newPassword(CodAuthenticatorDTO value) {
		try {
			User user = new User(-1, value.getUser().getEmail(), null, -1);
			user = daoU.query(daoU.queryId(user));
			if (user == null)
				throw new RegisteredUserException("unregistered user");
			Validator.validatePassword(value.getUser().getPassword());
			TokenResponseDTO token = new TokenResponseDTO(daoU.getToken(user), daoR.queryRoleForId(user.getId_role()), "");
			LoginResponseDTO cod = authService.login(token);
			if (!value.getCod().equals(cod.getEmail()))
				throw new InvalidTokenException("invalid code");
			user.setPassword(BCrypt.hashpw(value.getUser().getPassword(), BCrypt.gensalt()));
			daoU.updatePassword(user);
			return new LoginInputDTO(user.getEmail(), value.getUser().getPassword());
		}
		catch (SQLException e) {
			throw new RegisteredUserException("internal error accessing database");
		}
		catch (InvalidTokenException e) {
			throw new InvalidTokenException("expired code");
		}
	}
}
