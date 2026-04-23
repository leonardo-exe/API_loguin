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

/**
 * Classe de serviço que realiza alterações e consultas no banco de dados.
 */
@Service
public class UserService {
	@Autowired
	private DaoUser daoU;
	@Autowired
	private DaoRole daoR;
	@Autowired
	private AuthService authService;

	/**
	 * Verifica se uma permission existe para a role específica.
	 * @param id_role Chave primária da role.
	 * @param permission String com o nome da permission.
	 * @return Valor booleano.
	 * @throws SQLException Erro no acesso ao banco de dados.
	 */
	private boolean permissionExist(int id_role, String permission) throws SQLException {
		List<Permission> list = daoR.queryPermissionForRole(id_role);
		for (Permission i : list)
			if (i.getPermission().equals(permission))
				return true;
		return false;
	}

	/**
	 * Valida os dados de entrada e registra o usuário no sistema.
	 * @param value Classe DTO contendo email e senha.
	 * @throws InvalidEmailException Email em formato inválido.
	 * @throws RegisteredUserException Email já cadastrado.
	 * @throws InvalidPasswordException Formato de senha inválido.
	 */
	public void register(LoginInputDTO value) throws InvalidEmailException, RegisteredUserException, InvalidPasswordException {
		try {
			if (!Validator.validateEmail(value.getEmail()))
				throw new InvalidEmailException("invalid email");
			if (daoU.queryId(new User(-1, value.getEmail(), value.getPassword(), -1)) != -1)
				throw new RegisteredUserException("There is already a registration with this email address.");
			Validator.validatePassword(value.getPassword());
			String hash = BCrypt.hashpw(value.getPassword(), BCrypt.gensalt());
			User user = new User(-1, value.getEmail(), hash, 1);
			daoU.insert(user);
			new LoginResponseDTO(user.getEmail(), "User");
		}
		catch (SQLException e) {
			throw new RegisteredUserException("internal error accessing database");
		}
	}

	/**
	 * Define um nível de permissão (role) para outro usuário.
	 * @param request Classe DTO contendo um usuário de permissão superior ao que vai ser alterado.
	 * @param value Classe DTO contendo o usuário que deve ser alterado.
	 * @param role Novo nível de permissão desejado.
	 * @return Classe DTO com email e role definida para o usuário.
	 * @throws RegisteredUserException Usuário não encontrado.
	 * @throws NotAllowedException Erro de permissão, pode estar tentando definir um usuário para nível superior ao próprio.
	 */
	public LoginResponseDTO defineRole(LoginResponseDTO request, LoginResponseDTO value, String role) throws RegisteredUserException, NotAllowedException {
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

	/**
	 * Deleta um usuário do sistema.
	 * @param request Classe DTO contendo um usuário com permissão para deletar (admin).
	 * @param value Classe DTO contendo o usuário que deve ser deletado.
	 * @return Classe DTO com os dados do usuário requerente.
	 * @throws RegisteredUserException Usuário não encontrado.
	 * @throws NotAllowedException Requerente não é um admin.
	 */
	public LoginResponseDTO delete(LoginResponseDTO request, LoginResponseDTO value) throws RegisteredUserException, NotAllowedException {
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

	/**
	 * Busca todos os usuários cadastrados no sistema.
	 * @param request Classe DTO contendo o usuário com permissão para consultar usuários.
	 * @return List de classes DTO contendo email e role de todos os usuários do sistema.
	 * @throws RegisteredUserException Usuário não encontrado.
	 * @throws NotAllowedException Usuário não possui nível moderador ou superior.
	 */
	public List<LoginResponseDTO> list(LoginResponseDTO request) throws RegisteredUserException, NotAllowedException {
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

	/**
	 * Altera a senha do usuário caso o código de autenticação esteja correto.
	 * @param value Classe DTO contendo código de autenticação e dados do usuário.
	 * @throws RegisteredUserException Usuário não encontrado.
	 * @throws InvalidTokenException Código errado ou expirado.
	 */
	public void newPassword(CodAuthenticatorDTO value) throws RegisteredUserException, InvalidTokenException {
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
			user.setPassword(null);
			daoU.recoverToken(user);
			new LoginInputDTO(user.getEmail(), value.getUser().getPassword());
		}
		catch (SQLException e) {
			throw new RegisteredUserException("internal error accessing database");
		}
		catch (InvalidTokenException e) {
			throw new InvalidTokenException("expired code");
		}
	}

	/**
	 * Registra uma nova senha para o usuário.
	 * @param value Classe DTO contendo email e nova senha.
	 * @return Classe DTO contendo email e role do usuário alterado.
	 * @throws InvalidPasswordException Senha em formato não aceito.
	 */
	public LoginResponseDTO newPassword(LoginInputDTO value) throws InvalidPasswordException {
		try {
			Validator.validatePassword(value.getPassword());
			User user = new User(-1, value.getEmail(), null, -1);
			user = daoU.query(daoU.queryId(user));
			String hashPassword = BCrypt.hashpw(value.getPassword(), BCrypt.gensalt());
			user.setPassword(hashPassword);
			daoU.updatePassword(user);
			return new LoginResponseDTO(user.getEmail(), daoR.queryRoleForId(user.getId_role()));
		}
		catch (SQLException e) {
			throw new RegisteredUserException("internal error accessing database");
		}
	}
}
