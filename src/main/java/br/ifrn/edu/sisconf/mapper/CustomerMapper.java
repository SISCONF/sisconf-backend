package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.dtos.CustomerCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.CustomerResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {PersonMapper.class})
public interface CustomerMapper {
    Customer toEntity(CustomerCreateRequestDTO customerCreateRequestDto);
    @Mapping(target = "person.address.city", source = "person.address.city.id")
    CustomerResponseDTO toResponse(Customer customer);
    List<CustomerResponseDTO> toListResponse(List<Customer> customers);
}
