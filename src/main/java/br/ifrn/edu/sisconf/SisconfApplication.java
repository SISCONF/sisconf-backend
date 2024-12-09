package br.ifrn.edu.sisconf;

import br.ifrn.edu.sisconf.utils.keycloak.KeycloakRealmsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SisconfApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisconfApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(@Autowired KeycloakRealmsClient keycloakRealmsClient) {
		return args -> {
			var teste = keycloakRealmsClient.getClientAccessToken();
			System.out.println(teste.getAccessToken());
		};
	}
}
