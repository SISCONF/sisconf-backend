package br.ifrn.edu.sisconf;

import br.ifrn.edu.sisconf.utils.keycloak.KeycloakRealmsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@SpringBootApplication
public class SisconfApplication {

	public static void main(String[] args) {
		SpringApplication.run(SisconfApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(@Autowired KeycloakRealmsClient KeycloakRealmsClient) {
		return args -> {
			try {
				var teste = KeycloakRealmsClient.getClientAccessToken();
			} catch (WebClientResponseException exception) {
				if (exception.getStatusCode().is4xxClientError()) {
					System.out.println(exception.getStatusCode().value());
				}
			}
		};
	}
}
