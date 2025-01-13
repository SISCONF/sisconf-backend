package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerResponseDTO;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {PersonMapper.class})
public interface CustomerMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "person.id", ignore = true)
    @Mapping(target = "person.createdAt", ignore = true)
    @Mapping(target = "person.updatedAt", ignore = true)
    @Mapping(target = "person.keycloakId", ignore = true)
    @Mapping(target = "person.address.id", ignore = true)
    @Mapping(target = "person.address.createdAt", ignore = true)
    @Mapping(target = "person.address.updatedAt", ignore = true)
    @Mapping(target = "person.address.city.id", source = "person.address.city")
    Customer toEntity(CustomerRequestDTO customerRequestDTO);

    @Mapping(target = "person.address.city", source = "person.address.city.id")
    CustomerResponseDTO toResponseDTO(Customer customer);

    List<CustomerResponseDTO> toDTOList(List<Customer> customers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "person.address.id", ignore = true)
    @Mapping(target = "person.address.createdAt", ignore = true)
    @Mapping(target = "person.address.updatedAt", ignore = true)
    @Mapping(target = "person.id", ignore = true)
    @Mapping(target = "person.createdAt", ignore = true)
    @Mapping(target = "person.updatedAt", ignore = true)
    @Mapping(target = "person.keycloakId", ignore = true)
    @Mapping(source = "person.address.city", target = "person.address.city.id")
    void updateEntityFromDTO(CustomerRequestDTO customerUpdateRequestDTO, @MappingTarget Customer customer);
}
