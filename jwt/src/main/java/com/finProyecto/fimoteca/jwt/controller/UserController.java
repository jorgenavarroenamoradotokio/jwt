package com.finProyecto.fimoteca.jwt.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finProyecto.fimoteca.jwt.negocio.security.jwt.JwtRequest;
import com.finProyecto.fimoteca.jwt.negocio.security.jwt.JwtResponse;
import com.finProyecto.fimoteca.jwt.negocio.security.jwt.JwtTokenUtil;
import com.finProyecto.fimoteca.jwt.negocio.usuario.GestorUsuario;
import com.finProyecto.fimoteca.jwt.persistencia.user.Role;
import com.finProyecto.fimoteca.jwt.persistencia.user.User;
import com.finProyecto.fimoteca.jwt.vo.Response;
import com.finProyecto.fimoteca.jwt.vo.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	GestorUsuario gestorUsuario;
	
	
	/**
	 * 
	 * Iniciamos el proceso de inicio de sesion en nuestros sistemas
	 * 
	 * @param authRequest
	 * @return
	 * @throws Exception
	 */
	
	@Operation(summary = "Inicio de sesion")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "Token de inicio de sesion correcto", content = @Content(array = @ArraySchema(schema=@Schema(implementation = JwtResponse.class)))),
			@ApiResponse(responseCode = "401" , description = "Usuario/password incorrecto", content = @Content(array = @ArraySchema(schema=@Schema(implementation = JwtResponse.class)))),
			@ApiResponse(responseCode = "101" , description = "Error no controlado", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class))))
	})
	
	@PostMapping(value="/login" , consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) throws Exception {
		logger.info("Iniciamos el proceso de login en la app");
		try {
			authentication(authRequest.getUsername(), authRequest.getPassword());
			final String token = jwtTokenUtil.generadorToken(authRequest.getUsername());
			return ResponseEntity.ok(Response.builder().status(String.valueOf(HttpStatus.OK.value())).msg("OK").data(new JwtResponse(token)).build());
		} catch (DisabledException | BadCredentialsException ex) {
			return ResponseEntity.ok(Response.builder().status("401").msg("Disabled user o badCredentials insert").build());
		}
	}
	
	/**
	 * 
	 * Iniciamos el proceso de registro de informacion de un usuario en nuestros sistemas
	 * 
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	
	@Operation(summary = "Registro de usuario en nuestros sistemas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "Registro de usuario correcto", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class)))),
			@ApiResponse(responseCode = "401" , description = "El usuario ya existe", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class)))),
			@ApiResponse(responseCode = "101" , description = "Error no controlado", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class))))
	})
	
	@PostMapping(value = "/signin", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> register(@RequestBody UserDTO user) throws Exception{
		logger.info("Iniciamos el proceso de registro de usuario");
		
		// Comprobamos que el usuario solicitado no este ya registrado en nuestros sistemas
		if (!gestorUsuario.existeUser(user.getUsername())) {
			gestorUsuario.registrarUsuario(user);
			logger.info("Registro completado con exito");
			return ResponseEntity.ok(Response.builder().status(String.valueOf(HttpStatus.OK.value())).msg("Registro completado con exito").data(user).build());
		} else {
			logger.warn("El username insertado ya esta registrado. Lo usa otro usario");
			return ResponseEntity.ok(Response.builder().status(String.valueOf(HttpStatus.UNAUTHORIZED.value())).msg("Usuario ya existente").build());
		}
	}
	
	/**
	 * 
	 * Iniciamos el proceso de consulta de todos los roles registrados en nuestros sistemas
	 * 
	 * @return
	 * @throws Exception
	 */
	
	@Operation(summary = "Obtenemos todos los roles registrados en nuestros sistemas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "Listado de roles", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class)))),
			@ApiResponse(responseCode = "404" , description = "No se ha encontrado ningun rol en nuestros sistemas", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class)))),
			@ApiResponse(responseCode = "101" , description = "Error no controlado", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class))))
	})
	
	@GetMapping(value = "/roles", produces = "application/json")
	public ResponseEntity<?> roles() throws Exception {
		logger.info("Iniciamos el proceso de consulta de todos los roles");
		Set<Role> rol = gestorUsuario.buscarTodosRoles();
		if(rol != null && !rol.isEmpty()) {
			logger.debug("Obtenemos todos los roles");
			return ResponseEntity.ok(Response.builder().status(String.valueOf(HttpStatus.OK.value())).msg("Registro completado con exito").data(rol).build());
		} else {
			logger.warn("No se han localizado ningun rol en el sistema");
			return ResponseEntity.ok(Response.builder().status(String.valueOf(HttpStatus.NOT_FOUND.value())).msg("No se han encontrado roles registrados en el sistema").build());
		}
	}
	
	/**
	 * 
	 * Iniciamos el proceso de busqueda de rol asociado a un usuario
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	
	@Operation(summary = "Obtenemos el rol de un usuario")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "Rol de un usuario", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class)))),
			@ApiResponse(responseCode = "404" , description = "No se ha encontrado ningun rol asociado al usuario", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class)))),
			@ApiResponse(responseCode = "101" , description = "Error no controlado", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class))))
	})
	
	@PostMapping(value = "/user-rol", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> rolUsuario(@RequestBody User user) throws Exception {
		logger.info("Iniciamos el proceso de consulta de rol asociado a un usuario");
		String rol = gestorUsuario.obtenerRolAsociadoUsuario(user.getUsername());
		if (rol != null && !rol.isEmpty()) {
			logger.info("Retornamos el rol del usuario");
			return ResponseEntity.ok(Response.builder().status(String.valueOf(HttpStatus.OK.value())).msg("Rol encontrado").data(rol).build());
		} else {
			logger.info("No se ha encontrado ningun rol asociado al usuario");
			return ResponseEntity.ok(Response.builder().status(String.valueOf(HttpStatus.NOT_FOUND.value())).msg("No se ha encontrado rol asociado").build());
		}
	}
	
	/**
	 * 
	 * Iniciamos el proceso de marcar la hora del ultimo login
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	
	@Operation(summary = "Actualizamos la ultima hora de conexsion del usuario")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "Fecha login actualizada", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class)))),
			@ApiResponse(responseCode = "101" , description = "Error no controlado", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class))))
	})
	@PostMapping(value = "/update-dateLogin", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> actualizarUltimaFechaLogin(@RequestBody UserDTO user) throws Exception {
		logger.info("Iniciamos el proceso de actualizar la fecha de login");
		gestorUsuario.actualizarFechaLogin(user);
		return ResponseEntity.ok(Response.builder().status(String.valueOf(HttpStatus.OK)).msg("fecha actualizada").build());
	}
	
	
	/**
	 * 
	 * Iniciamos el proceso de marcar la hora del ultimo login
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	
	@Operation(summary = "Obtenemos el id del usuario conectado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "ID usuario conectado", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class)))),
			@ApiResponse(responseCode = "101" , description = "Error no controlado", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class))))
	})
	@PostMapping(value = "/user-id", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> getIdUsuario(@RequestBody UserDTO user) throws Exception {
		logger.info("Obtenemos el id del usuario conectado");
		return ResponseEntity.ok(Response.builder().status(String.valueOf(HttpStatus.OK)).msg("fecha actualizada").data(gestorUsuario.getIdUsuario(user)).build());
	}
	
	/**
	 * 
	 * En la configuracion inyectamos un objeto que implementa la interfaz AuthenticationManager.
	 * Dicha interfaz nos permite implementar de UserDetailsService el metodo para validad la persona
	 * comparar el password de la peticion con la que esta almacenada en nuestros sistemas (codificada en bbdd) 
	 * 
	 * @param username
	 * @param pwd
	 * @throws Exception
	 */
	
	private void authentication(String username, String pwd) throws BadCredentialsException, DisabledException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, pwd));
		}catch(DisabledException ex) {
			logger.error("DisabledException: ", ex);
			throw ex;
		}catch(BadCredentialsException bce) {
			logger.error("BadCredentialsException: ", bce);
			throw bce;
		}
	}
}