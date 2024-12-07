//package br.ifrn.edu.sisconf.utils.keycloak;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Optional;
//
//@Getter
//@Setter
//@Component
//public class KeycloakClient {
//    private KeycloakProperties keycloakProperties;
//    private String baseURL;
//    private WebClient webClient;
//    private String accessToken;
//
//    @Autowired
//    public KeycloakClient(KeycloakProperties keycloakProperties) {
//        this.keycloakProperties = keycloakProperties;
//        this.baseURL = keycloakProperties.getBaseUrl() + "/admin/realms/" + keycloakProperties.getRealmName();
//        this.webClient = buildWebClient();
//    }
//
//    private WebClient buildWebClient() {
//        return WebClient.builder()
//                .baseUrl(this.baseURL)
//                .defaultHeaders(httpHeaders -> {
//                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//                })
//                .build();
//    }
//
//    public GetClientTokenResponseDTO getClientAccessToken() {
//        var getAccessTokenReqBody = new GetClientTokenRequestDTO(
//                this.keycloakProperties.getClientId(),
//                this.keycloakProperties.getClientSecret()
//        );
//        return this.webClient
//                .post()
//                .uri("/protocol/openid-connect/token/")
//                .bodyValue(getAccessTokenReqBody)
//                .retrieve()
//                .bodyToMono(GetClientTokenResponseDTO.class)
//                .block();
//    }
//}
