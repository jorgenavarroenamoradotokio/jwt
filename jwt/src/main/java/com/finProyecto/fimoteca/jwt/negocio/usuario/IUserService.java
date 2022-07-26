package com.finProyecto.fimoteca.jwt.negocio.usuario;

import java.util.Set;

import com.finProyecto.fimoteca.jwt.negocio.exception.NotFoundException;
import com.finProyecto.fimoteca.jwt.persistencia.user.Role;
import com.finProyecto.fimoteca.jwt.persistencia.user.User;
import com.finProyecto.fimoteca.jwt.vo.UserDTO;

public interface IUserService {

	Set<Role> buscarTodosRoles();
	Role buscarRolPorId(long id)throws NotFoundException ;
	Role buscarRolPorNombre(String nombre) throws NotFoundException;
	String obtenerRolAsociadoUsuario(String username) throws NotFoundException;
	
	
	boolean existeUser(String username);
	User registrarUsuario(UserDTO user) throws Exception;
	void actualizarFechaLogin(UserDTO user) throws Exception;
	long getIdUsuario(UserDTO user) throws Exception;
}
