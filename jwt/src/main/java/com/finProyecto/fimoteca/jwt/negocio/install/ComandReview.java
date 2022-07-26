package com.finProyecto.fimoteca.jwt.negocio.install;

import java.time.LocalDate;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.finProyecto.fimoteca.jwt.persistencia.user.IRolRepository;
import com.finProyecto.fimoteca.jwt.persistencia.user.IUserRepository;
import com.finProyecto.fimoteca.jwt.persistencia.user.Role;
import com.finProyecto.fimoteca.jwt.persistencia.user.User;

public class ComandReview {

	private final Logger logger = LoggerFactory.getLogger(ComandReview.class);

	@Autowired
	private IRolRepository rolRepository;

	@Autowired
	private IUserRepository userRepository;


	public void init() {
		logger.info("Iniciamos el proceso de alta de roles");
		// Definimos los perfiles admin
		Role rolAdmin = rolRepository.save(Role.builder().name("ADMIN").build());
		rolRepository.save(Role.builder().name("USER").build());

		BCryptPasswordEncoder bcy = new BCryptPasswordEncoder();
		
		// Creamos el usuario
		logger.info("Creamos el usuario administrador por defecto");
		userRepository.save(User.builder().active(true).birthDate(Calendar.getInstance().getTime())
				.creationDate(LocalDate.now()).email("asd@asd.com").lastLogin(null).name("user test")
				.password(bcy.encode("1234")).surname("testing").username("Test").rol(rolAdmin).build());
	}
}
