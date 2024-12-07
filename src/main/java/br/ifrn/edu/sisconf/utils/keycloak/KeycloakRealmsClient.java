package br.ifrn.edu.sisconf.utils.keycloak;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Getter
@Setter
@Component
public class KeycloakRealmsClient {
    private KeycloakProperties keycloakProperties;
    private String baseURL;
    private WebClient webClient;
    private String accessToken;

    @Autowired
    public KeycloakRealmsClient(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
        this.baseURL = keycloakProperties.getBaseUrl() + "/realms/" + keycloakProperties.getRealmName();
        this.webClient = buildWebClient();
    }

    private WebClient buildWebClient() {
        HttpClient client = HttpClient.create().responseTimeout(Duration.ofSeconds(2));
        return WebClient.builder()
                .baseUrl(this.baseURL)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
                })
                .clientConnector(new ReactorClientHttpConnector(client))
                .build();
    }

    public GetClientTokenResponseDTO getClientAccessToken() throws WebClientResponseException {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", this.keycloakProperties.getClientId());
        formData.add("client_secret", this.keycloakProperties.getClientSecret());
        formData.add("grant_type", "client_credentials");

        return this.webClient
                .post()
                .uri("/protocol/openid-connect/token/")
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(GetClientTokenResponseDTO.class)
                .block();
    }
}
