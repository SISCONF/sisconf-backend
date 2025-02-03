package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.domain.dtos.CityResponseDTO;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CityMapper {
    @Mapping(target = "countryState", source = "countryState.id")
    CityResponseDTO toResponse(City city);
    
    @Mapping(target = "countryState", source = "countryState.id")
    List<CityResponseDTO> toDTOList(List<City> cities);
}
