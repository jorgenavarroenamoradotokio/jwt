package com.finProyecto.fimoteca.jwt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.finProyecto.fimoteca.jwt.negocio.exception.DuplicateDataException;
import com.finProyecto.fimoteca.jwt.negocio.exception.NotFoundException;
import com.finProyecto.fimoteca.jwt.utils.Constantes;
import com.finProyecto.fimoteca.jwt.vo.Response;

@ControllerAdvice
public class ExceptionController {

	private final static Logger logger = LoggerFactory.getLogger(ExceptionController.class);
	
	/**
	 * 
	 * Tratamos todas las excepciones de tipo DuplicateDataException para indicar al
	 * usuario que se ha producido un error de informacion duplicada
	 * 
	 * @param exception
	 * @return
	 */
	
	@ExceptionHandler(DuplicateDataException.class)
	public ResponseEntity<Response<Object>> hadelExceptionDuplicate(Exception exception) {
		logger.error("Se ha producido un error de dpulicidad", exception);
		Response<Object> response = construirRespuestaError(Constantes.ERROR_DUPLICADO_REGISTRO, exception.getMessage(), exception);
		return new ResponseEntity<Response<Object>>(response, HttpStatus.OK);
	}
	
	/**
	 * 
	 * Tratamos todas las excepciones de tipo NotFoudException para indicar al
	 * usuario que se ha producido un error de informacion no encontrada
	 * 
	 * @param exception
	 * @return
	 */
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Response<Object>> hadelExceptionNotFound(Exception exception) {
		logger.error("Se ha producido un error inexperado", exception);
		Response<Object> response = construirRespuestaError(Constantes.ERROR_NO_ENCONTRADO, exception.getMessage(), exception);
		return new ResponseEntity<Response<Object>>(response, HttpStatus.OK);
	}
	
	/**
	 * 
	 * Tratamos todas las excepciones genericas para indicar al usuario que se ha producido un error inexperado.
	 * 
	 * @param exception
	 * @return
	 */
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response<Object>> hadelException(Exception exception) {
		logger.error("Se ha producido un error inexperado", exception);
		Response<Object> response = construirRespuestaError(Constantes.ERROR_GENERICO, exception.getMessage(), exception);
		return new ResponseEntity<Response<Object>>(response, HttpStatus.OK);
	}
	
	/**
	 * 
	 * Construimos el objeto de respuesta para notificar el tipo de error, el mensaje y la informacion de manera mas detallada
	 * 
	 * @param codeError
	 * @param mensaje
	 * @param obj
	 * @return
	 */

	private Response<Object> construirRespuestaError(String codeError, String mensaje, Object obj) {
		Response<Object> response = Response.builder()
				.status(codeError)
				.msg(mensaje)
				.data(obj)
				.build();
		return response;
	}
}