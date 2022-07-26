package com.finProyecto.fimoteca.jwt.persistencia.review;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.finProyecto.fimoteca.jwt.persistencia.user.IRolRepository;
import com.finProyecto.fimoteca.jwt.persistencia.user.IUserRepository;
import com.finProyecto.fimoteca.jwt.persistencia.user.Role;
import com.finProyecto.fimoteca.jwt.persistencia.user.User;

@SpringBootTest
@RunWith(JUnit4.class)
class TestReviewPersistencia {

	@Autowired
	IReviewRepository repository;
	
	@Autowired
	IRolRepository rolRepository;

	@Autowired
	IUserRepository userRepository;
	
	private User user;
	private Role rol;
	
	@BeforeEach
	public void initDatos() {
		rol = rolRepository.save(Role.builder().name("TEST").build());
		user = userRepository.save(User.builder().active(true).birthDate(new Date()).email("asd@gmail.com").name("test").password("prueba").username("test").rol(rol).build());
	}

	@AfterEach
	public void restaurarDatos() {
		repository.deleteAll();
		rolRepository.delete(rol);
		userRepository.delete(user);
	}

	@Test
	public void schemaExecutionTest() {
		try {
			registrarReview();
			busquedaReviewAsociadosUsuarionSinReview();
			busquedaReviewAsociadosUsuarionConReview();
			existeComentarioAsociadoPeliculaParaUnUsuario();
			noExisteComentarioAsociadoPeliculaParaUnUsuario();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}
	
	private void registrarReview() {
		repository.save(Review.builder().date(Calendar.getInstance().getTime()).film(1L).textReview("Comentario").title("Titulo").user(user).build());
	}
	
	private void busquedaReviewAsociadosUsuarionSinReview() {
		Set<Review> listado = repository.comentariosAsociadoUsuario(999L);
		assertEquals(listado.isEmpty(), true);
	}
	
	private void busquedaReviewAsociadosUsuarionConReview() {
		Set<Review> listado = repository.comentariosAsociadoUsuario(user.getId());
		assertEquals(listado.isEmpty(), false);
	}
	
	private void existeComentarioAsociadoPeliculaParaUnUsuario() {
		boolean existe = repository.existeFilmComentario(1, user.getId());
		assertEquals(existe, true);
	}
	
	private void noExisteComentarioAsociadoPeliculaParaUnUsuario() {
		boolean existe = repository.existeFilmComentario(1, 999L);
		assertEquals(existe, false);
	}
}