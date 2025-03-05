package br.ifrn.edu.sisconf.domain.dtos.Person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String authenticationToken;
    private String refreshToken;
    private Long expiresIn;
    private Long refreshesIn;
    private String tokenType;
    private Integer notBeforePolicy;
    private String sessionState;
    private String scope;
}
