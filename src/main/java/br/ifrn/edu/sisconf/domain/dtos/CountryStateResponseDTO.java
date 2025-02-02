package br.ifrn.edu.sisconf.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryStateResponseDTO {
    private Long id;
    private String fullname;
    private String abbreviation;
}
