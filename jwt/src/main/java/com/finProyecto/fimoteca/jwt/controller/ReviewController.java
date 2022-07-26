package com.finProyecto.fimoteca.jwt.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finProyecto.fimoteca.jwt.negocio.exception.DuplicateDataException;
import com.finProyecto.fimoteca.jwt.negocio.exception.NotFoundException;
import com.finProyecto.fimoteca.jwt.negocio.review.IReviewService;
import com.finProyecto.fimoteca.jwt.vo.Response;
import com.finProyecto.fimoteca.jwt.vo.ReviewDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class ReviewController {

	private final Logger logger = LoggerFactory.getLogger(ReviewController.class);
	
	@Autowired
	IReviewService service;


	/**
	 * 
	 * Iniciamos el proceso de alta de una nueva review en nuestros sistemas
	 * 
	 * @param reviewDTO
	 * @return
	 * @throws DuplicateDataException
	 */
	
	@Operation(summary = "Registro de una review")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "Registro de una review completado con exito", content = @Content(array = @ArraySchema(schema=@Schema(implementation = String.class)))),
			@ApiResponse(responseCode = "201" , description = "Operacion completada, pero no se registro informacion, datos duplicado", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class))))
	})
	
	@PostMapping(value = "/new-review", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> alta(@RequestBody ReviewDTO reviewDTO) throws DuplicateDataException {
		// Comprobamos si el usuario ya tiene una review para la pelicula seleccionada
		logger.info("Iniciamos el proceso de registro de una review en nuestros sistemas");
		boolean existe = service.existeReviewDelUsuarioAsociadaPelicula(reviewDTO);
		if (existe) {
			logger.warn("Se ha comprobado que el usuario ya tenia un comentario asociado");
			throw new DuplicateDataException("Ya ha registrado una critica para esa pelicula");
		}
		service.registrarReview(reviewDTO);
		logger.info("Registro completado con exito");
		return ResponseEntity.ok(Response.builder().status(String.valueOf(HttpStatus.OK.value())).msg("OK").build());
	}
	
	@Operation(summary = "Obtenemos todas los comentarios asociados a la pelicula")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "Devuelve un listado con todas las opinones asociadas a una pelicula", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class)))),
	})
	@GetMapping(value="/review-consulting/{id}")
	public ResponseEntity<?> obtenerReviewAsociadaPelicula(@PathVariable("id") long idFilm) throws Exception {
		logger.info("Iniciamos la busqueda de todas las reviews asociadas a una pelicula");
		return ResponseEntity.ok(Response.builder().status(String.valueOf(HttpStatus.OK.value())).msg("OK").data(service.obtenerReviewsAsociadasPeliculas(idFilm)).build());
	}
	
	/**
	 * 
	 * Peticion encargada de llamar a la logica de negocio para obtener el listado de reviews asociadas a un usuario.
	 * En caso de no tener ninguna review asociada se lanzara la Excepcion NotFoundException
	 * 
	 * @param id
	 * @return
	 * @throws NotFoundException
	 */
	
	@Operation(summary = "Obtenemos todas las reviews asociadas a un usuario")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200" , description = "Listado de reviews asociados un usuario", content = @Content(array = @ArraySchema(schema=@Schema(implementation = ReviewDTO.class)))),
			@ApiResponse(responseCode =  "404" , description = "Operacion completada, pero no se obtuvo reviews asociadas a un usuario", content = @Content(array = @ArraySchema(schema=@Schema(implementation = Response.class))))
	})
	
	@GetMapping(value = "/user/{id}/reviews")
	public ResponseEntity<?> consultaReviewUsuario(@PathVariable(name = "id") long id) throws NotFoundException {
		logger.info("Iniciamos el proceso de obtener todas las reviews asociadas a un usuario");
		Set<ReviewDTO> review = service.obtenerReviewsAsociadasUsuario(id);
		if(review == null || review.isEmpty()) {
			logger.warn("No se han encontrado reviews asociadas al usuario de busqueda");
			throw new NotFoundException("El usario no tiene datos asociados");
		}
		logger.info("Busqueda completada con exito");
		return ResponseEntity.ok(review);
	}	
}