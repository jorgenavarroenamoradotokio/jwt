package com.finProyecto.fimoteca.jwt.negocio.review;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.finProyecto.fimoteca.jwt.persistencia.review.IReviewRepository;
import com.finProyecto.fimoteca.jwt.persistencia.review.Review;
import com.finProyecto.fimoteca.jwt.persistencia.user.User;
import com.finProyecto.fimoteca.jwt.vo.ReviewDTO;

@SpringBootTest
@RunWith(JUnit4.class)
class TestGestorReview {

	@Autowired
	ModelMapper mapper;
	
	@Mock
	IReviewRepository repositoryMock;

	@Test
	public void existsReviewFilmUser() {
		when(repositoryMock.existeFilmComentario(Mockito.anyLong(), Mockito.anyLong())).thenReturn(true);

		GestorReview gestorReview = new GestorReview();
		gestorReview.repository = repositoryMock;
		boolean existe = gestorReview
				.existeReviewDelUsuarioAsociadaPelicula(ReviewDTO.builder().film(1L).user(1L).build());

		assertEquals(existe, true);
	}

	@Test
	public void noExistsReviewFilmUser() {
		when(repositoryMock.existeFilmComentario(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);

		GestorReview gestorReview = new GestorReview();
		gestorReview.repository = repositoryMock;
		boolean existe = gestorReview
				.existeReviewDelUsuarioAsociadaPelicula(ReviewDTO.builder().film(1L).user(1L).build());

		assertEquals(existe, false);
	}
	
	
	@Test
	public void existsReviewJoinUser() {
		when(repositoryMock.comentariosAsociadoUsuario(Mockito.anyLong())).thenReturn(listadoReviews());

		GestorReview gestorReview = new GestorReview();
		gestorReview.repository = repositoryMock;
		gestorReview.mapper = mapper;
		Set<ReviewDTO> listado = gestorReview.obtenerReviewsAsociadasUsuario(1L);
		assertEquals(listado.isEmpty(), false);
	}

	@Test
	public void noExistsReviewJoinUser() {
		when(repositoryMock.comentariosAsociadoUsuario(Mockito.anyLong())).thenReturn(null);

		GestorReview gestorReview = new GestorReview();
		gestorReview.repository = repositoryMock;
		gestorReview.mapper = mapper;
		Set<ReviewDTO> listado = gestorReview.obtenerReviewsAsociadasUsuario(1L);
		assertEquals(listado.isEmpty(), true);
	}
	

	private Set<Review> listadoReviews() {
		Set<Review> reviews = new HashSet<>();
		reviews.add(
				Review.builder().film(1L).user(User.builder().id(1L).build()).date(Calendar.getInstance().getTime()).textReview("text").title("titulo").build());
		return reviews;
	}
}