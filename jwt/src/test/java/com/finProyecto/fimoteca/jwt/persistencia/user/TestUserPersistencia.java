package com.finProyecto.fimoteca.jwt.persistencia.user;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@RunWith(JUnit4.class)
class TestUserPersistencia {

	@Autowired
	IUserRepository repository;

	@Autowired
	IRolRepository rolRepository;

	@Autowired
	BCryptPasswordEncoder encoder;

	@BeforeEach
	public void initDatos() {
		Role rol = rolRepository.save(Role.builder().name("USUARIO").build());
		repository.save(User.builder().active(true).birthDate(new Date()).email("asd@gmail.com").name("prueba").password(codificarPassowrd("prueba")).username("pruebaUsuario").rol(rol).build());
	}

	@AfterEach
	public void restaurarDatos() {
		repository.deleteAll();
		rolRepository.deleteAll();
	}

	@Test
	public void schemaExecutionTest() {
		try {
			buscarUsuarioPorNombreUsuario();
			buscarUsuarioPorNombreUsuarioNoExiste();
			buscarUsuarioPorUsuarioPasswordNoExiste();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	private void buscarUsuarioPorNombreUsuario() {
		assertEquals(repository.findUserByUsername("pruebaUsuario").isPresent(), true);
	}

	private void buscarUsuarioPorNombreUsuarioNoExiste() {
		assertEquals(repository.findUserByUsername("asd").isEmpty(), true);
	}

	private void buscarUsuarioPorUsuarioPasswordNoExiste() {
		assertEquals(repository.buscarPorNombreUsuarioYPassword("pruebaUsuario", "prueba").isPresent(), false);
	}

	private String codificarPassowrd(String pwd) {
		return encoder.encode(pwd);
	}
}