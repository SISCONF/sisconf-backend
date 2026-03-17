package br.ifrn.edu.sisconf.util;

import java.util.List;

import br.ifrn.edu.sisconf.domain.CountryState;
import br.ifrn.edu.sisconf.domain.dtos.CountryStateResponseDTO;

public class CountryStateTestUtil {
    public static List<CountryStateResponseDTO> toDTOList(List<CountryState> countryStates) {
        return countryStates.stream().map(countryState -> 
            new CountryStateResponseDTO(
                countryState.getId(),
                countryState.getFullname(),
                countryState.getAbbreviation()
            )
        ).toList();
    }
}
