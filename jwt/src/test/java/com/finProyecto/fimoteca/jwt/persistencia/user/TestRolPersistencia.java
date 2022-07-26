package com.finProyecto.fimoteca.jwt.persistencia.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;

@SpringBootTest
@RunWith(JUnit4.class)
class TestRolPersistencia {

	@Autowired
	IRolRepository repository;

	@BeforeEach
	public void initDatos() {
		System.out.println("Before");
		repository.save(Role.builder().name("USUARIO").build());
		repository.save(Role.builder().name("ADMIN").build());
	}

	@AfterEach
	public void restaurarDatos() {
		System.out.println("After");
		repository.deleteAll();
	}
	
	@Test
	public void schemaExecutionTest() {
		try {
			obtenerTodosRoles();
			obtenerRolPorNombre();
			obtenerRolPorNombreIncorrecto();	
		}catch(Exception ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}
	
	private void obtenerTodosRoles() {
		List<Role> roles = Streamable.of(repository.findAll()).toList();
		assertNotNull(roles);
		assertEquals(roles.isEmpty(), false);
	}

	
	private void obtenerRolPorNombre() {
		Optional<Role> rol = repository.findByName("USUARIO");
		assertEquals(rol.isPresent(), true);
	}

	
	private void obtenerRolPorNombreIncorrecto() {
		Optional<Role> rol = repository.findByName("VERIFICACION");
		assertEquals(rol.isPresent(), false);
	}
}
