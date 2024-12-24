package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.Address;
import br.ifrn.edu.sisconf.domain.dtos.AddressRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {
    @Mapping(source = "city", target = "city.id")
    Address toEntity(AddressRequestDTO addressCreateRequestDto);
}
