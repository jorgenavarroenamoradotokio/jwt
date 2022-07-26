package com.finProyecto.fimoteca.jwt.negocio.security.jwt;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.finProyecto.fimoteca.jwt.utils.Constantes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

	@Value("${jwt.secret}")
	private String clave;

	/**
	 * 
	 * Genera el token para un usuario
	 * 
	 * @param usuario Datos del usuario
	 * @return El token generado
	 */

	public String generadorToken(String usuario) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, usuario);
	}

	/**
	 * 
	 * Obtenemos la fecha de expiracion del token
	 * 
	 * @param token
	 * @return
	 */

	public Date getExpirationDate(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	/**
	 * 
	 * Obtenemos el usuario del token
	 * 
	 * @param token Un token JWT
	 * @return El usuario del token
	 */
	
	public String getUsuario(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	

	/**
	 * 
	 * Implementamos la llamada a la cabecera por referencia de metodo
	 * 
	 * @param <T>
	 * @param token
	 * @param claimsResolver
	 * @return
	 */
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	/**
	 * 
	 * Comprobamos si el token corresponde a un usuario concreto
	 * 
	 * @param token El token JWT
	 * @param userDetails los datos del usuario
	 * @return true si el token corresponde al usuario y este no ha expirado. False en caso contrario
	 */
	
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsuario(token);
		return username.equals(userDetails.getUsername()) && (!isTokenExpired(token));
	}
	
	/**
	 *
	 * Devuelve un objeto que implementa la interfaz Claims. Dicho objeto funciona
	 * parecido a un objeto tipo Map
	 * 
	 * @param token
	 * @return un objeto Claims
	 */

	private Claims getAllClaims(String token) {
		return Jwts.parser().setSigningKey(clave).parseClaimsJws(token).getBody();
	}

	/**
	 * 
	 * Generamos el token, con encriptacion de SignatureAlgorithm.HS512
	 * 
	 * @param claims  guardamos la informacion de expiracion del token, usuario,
	 *                token, etc
	 * @param subject datos del usuario
	 * @return
	 */

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Constantes.JWT_TOKEN_VALIDO * 1000))
				.signWith(SignatureAlgorithm.HS512, clave)
				.compact();
	}
	
	/**
	 * 
	 * Obtenemos si el token esta caducado o no
	 * 
	 * @param token
	 * @return
	 */
	
	private boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDate(token);
		return expiration.before(Calendar.getInstance().getTime());
	}
}