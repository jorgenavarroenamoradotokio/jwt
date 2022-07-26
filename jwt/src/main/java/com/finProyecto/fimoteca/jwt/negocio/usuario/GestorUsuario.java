package com.finProyecto.fimoteca.jwt.negocio.usuario;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.finProyecto.fimoteca.jwt.negocio.exception.NotFoundException;
import com.finProyecto.fimoteca.jwt.persistencia.user.IRolRepository;
import com.finProyecto.fimoteca.jwt.persistencia.user.IUserRepository;
import com.finProyecto.fimoteca.jwt.persistencia.user.Role;
import com.finProyecto.fimoteca.jwt.persistencia.user.User;
import com.finProyecto.fimoteca.jwt.vo.UserDTO;

@Service
public class GestorUsuario implements IUserService {

	private final Logger logger = LoggerFactory.getLogger(GestorUsuario.class);

	@Autowired
	IUserRepository userRepository;

	@Autowired
	IRolRepository rolRepository;

	@Autowired
	BCryptPasswordEncoder byc;
	
	@Autowired
	ModelMapper mapper;

	/**
	 * 
	 * Obtenemos todos los roles registrados en nuestros sistemas
	 * @return Set<Role> roles
	 */
	
	@Override
	public Set<Role> buscarTodosRoles() {
		logger.info("Obtenemos todos los roles registrados en nuestros sistemas");
		Set<Role> roles = new HashSet<>();
		rolRepository.findAll().forEach(roles::add);
		return roles;
	}

	/**
	 * 
	 * Obtenemos toda la informacion de un rol por ID
	 * En caso de no existir lanzamos una excepcion
	 * 
	 * @throws NotFoundException
	 * @return Role rol
	 * 
	 */
	
	@Override
	public Role buscarRolPorId(long id) throws NotFoundException {
		logger.info("Buscamos el rol por id");
		return rolRepository.findById(id).orElseThrow(() -> new NotFoundException());
	}

	/**
	 * 
	 * Obtenemos toda la informacion de un rol por su nombre.
	 * En caso de no existir dicha informacion en nuestros sistemas, lanzamos una excepcion
	 * 
	 * @throws NotFoundException
	 * @return Role rol
	 * 
	 */
	
	@Override
	public Role buscarRolPorNombre(String nombre) throws NotFoundException {
		logger.info("Buscamos el rol por nombre");
		return rolRepository.findByName(nombre).orElseThrow(() -> new NotFoundException());
	}
	
	/**
	 * 
	 * Obtenemos el rol asociado a un usuario
	 * 
	 * @throws NotFoundException 
	 * 
	 */
	
	@Override
	public String obtenerRolAsociadoUsuario(String username) throws NotFoundException {
		logger.info("Buscamos el rol asociado a un usuario");
		return rolRepository.obtenerRolAsociadoUsuario(username).orElseThrow(() -> new NotFoundException());
	}

	/**
	 * 
	 * Comprobamos si el nombre de usuario esta en nuestros sistemas
	 * 
	 * @return true en caso de que exista, false en caso de que no exista
	 * 
	 */
	
	@Override
	public boolean existeUser(String username) {
		logger.info("Buscamos si existe el nombre de usuario en base de datos");
		return userRepository.findUserByUsername(username).isPresent();
	}

	/**
	 * 
	 * Iniciamos el proceso de registro de la informacion de un usuario en nuestros
	 * sistemas.
	 * 
	 * @return Retornamos el usuario registrado en nuestros sistemas
	 * 
	 */
	
	@Override
	public User registrarUsuario(UserDTO userDTO) throws Exception {
		logger.info("Iniciamos el proceso de registro de usuario en la aplicacion");
		
		User user = mapperUser(userDTO);
		
		// Encriptamos la password
		user.setPassword(byc.encode(user.getPassword()));
		
		// Asociamos al usuario el rol preseleccionado
		if (user.getRol().getId() != 0) {
			user.setRol(buscarRolPorId(user.getRol().getId()));
		} else if (user.getRol().getName() != null) {
			user.setRol(buscarRolPorNombre(user.getRol().getName()));
		}
		
		// Indicamos que la cuenta del usuario esta activa
		user.setActive(true);
		user.setCreationDate(LocalDate.now());
		
		// Registramos el usario en la base de datos
		return userRepository.save(user);
	}

	@Override
	public void actualizarFechaLogin(UserDTO userDTO) throws Exception {
		logger.info("Iniciamos el proceso de actualizar la fecha de login de un usuario");
		User user = mapperUser(userDTO);
		userRepository.actualizarFechaLoginPorUsuario(user.getUsername(), user.getLastLogin());
	}
	
	@Override
	public long getIdUsuario(UserDTO userDTO) throws Exception {
		logger.info("Obtenemos el id del usuario");
		User u= userRepository.findUserByUsername(userDTO.getUsername()).orElseThrow(()-> new Exception());
		return u.getId();
	}
	
	
	private User mapperUser(UserDTO userDTO) {
		mapper.typeMap(UserDTO.class, User.class).addMappings(mapper -> mapper.skip(User::setRol));
		User user = mapper.map(userDTO, User.class);
		if(userDTO.getRol() != null) {
			user.setRol(Role.builder().id(userDTO.getRol().getId()).name(userDTO.getRol().getName()).build());	
		}
		
		return user;
	}
}
