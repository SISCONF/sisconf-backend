package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.dtos.CustomerCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.CustomerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    Customer toEntity(CustomerCreateRequestDTO customerCreateRequestDto);
    CustomerResponseDTO toResponse(Customer customer);
}
