package br.com.gs3tecnologia.usermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GerenciadorDeUsuariosEPerfisApplication {

	public static void main(String[] args) {
		SpringApplication.run(GerenciadorDeUsuariosEPerfisApplication.class, args);
	}

}
