package br.ifrn.edu.sisconf.domain.dtos;

import br.ifrn.edu.sisconf.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntrepreneurResponseDTO {
    private Long id;
    private PersonResponseDTO person;
    private String businessName;
}
