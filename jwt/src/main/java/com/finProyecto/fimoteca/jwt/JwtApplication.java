package com.finProyecto.fimoteca.jwt;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.finProyecto.fimoteca.jwt.negocio.install.ComandReview;

@SpringBootApplication
public class JwtApplication implements CommandLineRunner{

	private final Logger logger = LoggerFactory.getLogger(JwtApplication.class);
	
	@Autowired
	ComandReview commandReview;
	
	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<String> argsList = Arrays.asList(args);
		if (argsList != null && !argsList.isEmpty() && argsList.contains("install")) {
			logger.info("Iniciamos la carga de usuarios y roles por defecto");
			commandReview.init();
		}
	}
}