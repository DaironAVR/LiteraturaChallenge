package com.alura.challenge.literatura;

import com.alura.challenge.literatura.principal.Principal;
import com.alura.challenge.literatura.repositorio.LibroR;
import com.alura.challenge.literatura.repositorio.AutorR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {

	@Autowired
	private AutorR autorRepositorio;
	@Autowired
	private LibroR libroRepositorio;


	public static void main(String[] args) {

		SpringApplication.run(LiteraturaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(autorRepositorio,libroRepositorio);
		principal.muestraElMenu();

	}
}