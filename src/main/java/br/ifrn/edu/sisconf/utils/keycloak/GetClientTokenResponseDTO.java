package br.ifrn.edu.sisconf.utils.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetClientTokenResponseDTO {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("refresh_expires_in")
    private Integer refreshExpiresIn;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("not_before_policy")
    private Integer notBeforePolicy;
    @JsonProperty("scope")
    private String scope;
}
