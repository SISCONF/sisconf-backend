package br.ifrn.edu.sisconf.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import br.ifrn.edu.sisconf.domain.CountryState;
import br.ifrn.edu.sisconf.domain.dtos.CountryStateResponseDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CountryStateMapper {
    CountryStateResponseDTO toResponseDTO(CountryState countryState);
    List<CountryStateResponseDTO> toDTOList(List<CountryState> countryStates);
}
