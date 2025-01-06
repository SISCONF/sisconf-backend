package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.OrdersGroup;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroupRequestDTO;

import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = OrderMapper.class)
public interface OrdersGroupMapper {
    OrdersGroup toEntity(OrdersGroupRequestDTO ordersGroupRequestDTO);

}
