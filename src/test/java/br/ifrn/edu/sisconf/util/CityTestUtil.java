package br.ifrn.edu.sisconf.util;

import java.util.List;

import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.domain.dtos.CityResponseDTO;

public class CityTestUtil {
    public static List<CityResponseDTO> toDTOList(List<City> cities) {
        return cities.stream().map(city -> 
            new CityResponseDTO(
                city.getId(),
                city.getName(),
                city.getCountryState().getId()
            )
        ).toList();
    }
}
