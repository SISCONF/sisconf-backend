package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.Order;
import br.ifrn.edu.sisconf.domain.dtos.OrderRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toEntity(OrderRequestDTO dto);

    OrderResponseDTO toResponseDTO(Order order);

    List<OrderResponseDTO> toDTOList(List<Order> orders);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    void updateEntityFromDTO(OrderRequestDTO dto, @MappingTarget Order order);
}
