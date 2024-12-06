package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.Address;
import br.ifrn.edu.sisconf.domain.dtos.AddressCreateRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AddressMapper {
    Address toEntity(AddressCreateRequestDTO addressCreateRequestDto);
}
