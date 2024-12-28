package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.Order;
import br.ifrn.edu.sisconf.domain.dtos.OrderRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderUpdateRequestDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    Order toEntity(OrderRequestDTO orderRequestDTO);

    @Mapping(target = "customerId", source = "customer.id")
    OrderResponseDTO toResponseDTO(Order order);

    List<OrderResponseDTO> toDTOList(List<Order> orders);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(OrderUpdateRequestDTO orderUpdateRequestDTO, @MappingTarget Order order);
}
