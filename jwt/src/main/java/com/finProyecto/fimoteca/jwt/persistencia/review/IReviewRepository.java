package com.finProyecto.fimoteca.jwt.persistencia.review;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IReviewRepository extends CrudRepository<Review, Long> {

	@Query("select case when count(r) > 0 then true else false end from Review r where r.film = :idFilm and r.user.id = :idUser")
	boolean existeFilmComentario(@Param("idFilm") long idFilm, @Param("idUser") long idUser);

	@Query("select r from Review r where r.user.id = :idUser")
	Set<Review> comentariosAsociadoUsuario(@Param("idUser") long idUser);
	
	@Query("select r from Review r where r.film = :idFilm")
	Set<Review>obtenerReviewsAsociadasPeliculas(@Param("idFilm") long idFilm);
}
