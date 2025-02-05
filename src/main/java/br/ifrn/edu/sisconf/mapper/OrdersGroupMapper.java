package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.OrdersGroup;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface OrdersGroupMapper {
    OrdersGroup toEntity(OrdersGroupRequestDTO ordersGroupRequestDTO);

    OrdersGroupResponseDTO toResponseDTO(OrdersGroup ordersGroup);

    OrdersGroup updateOrdersGroup(OrdersGroupRequestDTO ordersGroupRequestDTO, @MappingTarget OrdersGroup ordersGroup);
}
