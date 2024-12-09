//package br.ifrn.edu.sisconf.utils.keycloak;
//
//import br.ifrn.edu.sisconf.utils.keycloak.dtos.CreateKeycloakUserRequestDTO;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//import reactor.netty.http.client.HttpClient;
//
//import java.time.Duration;
//
//@Getter
//@Setter
//@Component
//public class KeycloakAdminClient {
//    private KeycloakRealmsClient keycloakRealmsClient;
//    private KeycloakProperties keycloakProperties;
//    private String baseURL;
//    private WebClient webClient;
//    private String accessToken = "";
//
//    @Autowired
//    public KeycloakAdminClient(KeycloakRealmsClient keycloakRealmsClient, KeycloakProperties keycloakProperties) {
//        this.keycloakRealmsClient = keycloakRealmsClient;
//        this.keycloakProperties = keycloakProperties;
//        this.baseURL = keycloakProperties.getBaseUrl() + "admin/realms/" + keycloakProperties.getRealmName();
//        this.webClient = buildWebClient();
//    }
//
//    private WebClient buildWebClient() {
//        HttpClient client = HttpClient.create().responseTimeout(Duration.ofSeconds(2));
//        return WebClient.builder()
//                .baseUrl(this.baseURL)
//                .defaultHeaders(httpHeaders -> {
//                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//                })
//                .clientConnector(new ReactorClientHttpConnector(client))
//                .build();
//    }
//
////    public Mono<Boolean> createUser(CreateKeycloakUserRequestDTO createKeycloakUserRequestDTO) {
////        return this.webClient
////                .post()
////                .uri("/users/")
////                .bodyValue(createKeycloakUserRequestDTO)
////                .retrieve()
////                .onStatus(
////                        status -> !status.is2xxSuccessful(),
////
////                );
////    }
//}
