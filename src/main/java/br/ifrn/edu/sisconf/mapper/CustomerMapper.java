package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.dtos.CustomerCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.CustomerResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.CustomerUpdateRequestDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {PersonMapper.class})
public interface CustomerMapper {
    Customer toEntity(CustomerCreateRequestDTO customerCreateRequestDto);

    @Mapping(target = "person.address.city", source = "person.address.city.id")
    CustomerResponseDTO toResponse(Customer customer);

    List<CustomerResponseDTO> toListResponse(List<Customer> customers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(source = "person.address.city", target = "person.address.city.id")
    void updateEntityFromDTO(CustomerUpdateRequestDTO customerUpdateRequestDTO, @MappingTarget Customer customer);
}
