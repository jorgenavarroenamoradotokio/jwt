package com.finProyecto.fimoteca.jwt.negocio.review;

import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finProyecto.fimoteca.jwt.persistencia.review.IReviewRepository;
import com.finProyecto.fimoteca.jwt.persistencia.review.Review;
import com.finProyecto.fimoteca.jwt.persistencia.user.User;
import com.finProyecto.fimoteca.jwt.vo.ReviewDTO;

@Service
public class GestorReview implements IReviewService {

	private final Logger logger = LoggerFactory.getLogger(GestorReview.class);
	
	@Autowired
	ModelMapper mapper;

	@Autowired
	IReviewRepository repository;

	/**
	 * 
	 * Comprobamos si un usuario tiene un comentario sobre una pelicula
	 * 
	 * @return : true comentario realizado, false no tiene comentario realizado
	 */

	@Override
	public boolean existeReviewDelUsuarioAsociadaPelicula(ReviewDTO dto) {
		logger.info("Comprobamos si un usuario tiene un comentario sobre una pelicula");
		return repository.existeFilmComentario(dto.getFilm(), dto.getUser());
	}

	/**
	 * 
	 * Convertirmos el DTO en una entidad y registramos la informacion en
	 * nuestros sistemas
	 * 
	 */

	@Override
	public void registrarReview(ReviewDTO dto) {
		logger.info("Registramos la review en nuestros sistemas");
		
		// Transformamos el DTO en una entidad
		Review review = mapperObject(dto);
		logger.debug("La estructura del objeto es " + review.toString());
		
		// Registramos la informacion en base de datos
		repository.save(review);
	}

	/**
	 * 
	 * Obtenemos el listado de todas los comentarios que ha realizado un usuario en
	 * las diferentes peliculas existentes en la plataforma
	 * 
	 */

	@Override
	public Set<ReviewDTO> obtenerReviewsAsociadasUsuario(long idUser) {
		logger.info("Obtenemos el listado de todos los comentarios asociados a un usuario");
		Set<ReviewDTO> reviews = new HashSet<ReviewDTO>();
		Set<Review> reviewAux = repository.comentariosAsociadoUsuario(idUser);
		if (reviewAux != null && !reviewAux.isEmpty()) {
			reviewAux.stream().forEach(r -> reviews.add(mapperObject(r)));
		}
		return reviews;
	}
	
	/**
	 * 
	 * Obtenemos el listado de todos los comentarios que se han realizado sobre una
	 * pelicula
	 * 
	 */
	
	@Override
	public Set<ReviewDTO> obtenerReviewsAsociadasPeliculas(long idFilm) {
		logger.info("Obtenemos el listado de todos los comentarios asociados a una pelicula");
		Set<ReviewDTO> reviews = new HashSet<ReviewDTO>();
		Set<Review> reviewAux = repository.obtenerReviewsAsociadasPeliculas(idFilm);
		if (reviewAux != null && !reviewAux.isEmpty()) {
			reviewAux.stream().forEach(r -> reviews.add(mapperObject(r)));
		}
		return reviews;
	}
	

	/**
	 * 
	 * Conversor de DTO a Entidad
	 * 
	 * @param reviewDTO
	 * @return
	 */
	
	private Review mapperObject(ReviewDTO reviewDTO) {
		mapper.typeMap(ReviewDTO.class, Review.class).addMappings(mapper -> mapper.skip(Review::setUser));
		mapper.typeMap(ReviewDTO.class, Review.class).addMappings(mapper -> mapper.skip(Review::setFilm));
		Review review = mapper.map(reviewDTO, Review.class);
		review.setUser(User.builder().id(reviewDTO.getUser()).build());
		review.setFilm(reviewDTO.getFilm());
		return review;
	}

	/**
	 * 
	 * Conversor de entidad a DTO
	 * 
	 * @param review
	 * @return
	 */
	
	private ReviewDTO mapperObject(Review review) {
		mapper.typeMap(Review.class, ReviewDTO.class).addMappings(mapper -> mapper.skip(ReviewDTO::setUser));
		mapper.typeMap(Review.class, ReviewDTO.class).addMappings(mapper -> mapper.skip(ReviewDTO::setFilm));
		ReviewDTO reviewDTO = mapper.map(review, ReviewDTO.class);
		reviewDTO.setUser(review.getUser().getId());
		reviewDTO.setFilm(review.getFilm());
		return reviewDTO;
	}
}