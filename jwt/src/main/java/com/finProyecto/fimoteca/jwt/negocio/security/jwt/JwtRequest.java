package com.finProyecto.fimoteca.jwt.negocio.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {

	private String username;
	private String password;
	
}
