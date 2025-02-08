package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.OrdersGroup;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {OrderMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrdersGroupMapper {
    OrdersGroup toEntity(OrdersGroupRequestDTO ordersGroupRequestDTO);

    OrdersGroupResponseDTO toResponseDTO(OrdersGroup ordersGroup);

    @Mapping(target = "orders", ignore = true)
    OrdersGroup updateOrdersGroup(OrdersGroupRequestDTO ordersGroupRequestDTO, @MappingTarget OrdersGroup ordersGroup);
}
