package com.finProyecto.fimoteca.jwt.negocio.review;

import java.util.Set;

import com.finProyecto.fimoteca.jwt.vo.ReviewDTO;

public interface IReviewService {

	public boolean existeReviewDelUsuarioAsociadaPelicula(ReviewDTO dto);

	public void registrarReview(ReviewDTO dto);
	
	public Set<ReviewDTO> obtenerReviewsAsociadasUsuario(long idUser);
	
	public Set<ReviewDTO> obtenerReviewsAsociadasPeliculas(long idFilm);

}
