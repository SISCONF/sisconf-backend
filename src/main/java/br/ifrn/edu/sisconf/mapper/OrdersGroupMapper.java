package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.OrdersGroup;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrdersGroupMapper {
    OrdersGroup toEntity(OrdersGroupRequestDTO ordersGroupRequestDTO);

    OrdersGroupResponseDTO toResponseDTO(OrdersGroup ordersGroup);

    List<OrdersGroupResponseDTO> toDTOList(List<OrdersGroup> ordersGroups);

    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "orderDate", ignore = true)
    @Mapping(target = "docUrl", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "itemQuantity", ignore = true)
    OrdersGroup updateOrdersGroup(OrdersGroupRequestDTO ordersGroupRequestDTO, @MappingTarget OrdersGroup ordersGroup);
}
