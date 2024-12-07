package br.ifrn.edu.sisconf.utils.keycloak;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {
    private String baseUrl;
    private String realmName;
    private String clientId;
    private String clientSecret;
}
