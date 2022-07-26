package com.finProyecto.fimoteca.jwt.utils;

public class Constantes {

	// Constantes para definir las tablas de la base de datos
	public static final String TABLA_USERS = "users";
	public static final String TABLA_ROLES = "roles";
	public static final String TABLA_COMENTARIOS = "reviews";
	public static final String TABLA_FILMS = "films";

	// Definimos el tiempo de expiracion del token generado (5 horas)
	public static final long JWT_TOKEN_VALIDO = 5 * 60 * 60;

	// Errores de uso en la app
	public static final String NO_ERROR = "0";
	public static final String ERROR_GENERICO = "101";
	public static final String ERROR_DUPLICADO_REGISTRO = "201";
	public static final String ERROR_NO_ENCONTRADO = "401";

}
