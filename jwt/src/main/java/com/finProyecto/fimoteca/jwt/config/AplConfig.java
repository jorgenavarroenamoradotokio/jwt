package com.finProyecto.fimoteca.jwt.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.finProyecto.fimoteca.jwt.negocio.install.ComandReview;

@Configuration
public class AplConfig {

	/**
	 * 
	 * Bean encargado de instanciar las dependencias del ModelMapper para su uso en
	 * toda la app
	 * 
	 * @return
	 */

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public ComandReview getComandReview() {
		return new ComandReview();
	}
}
