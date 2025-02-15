package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.OrderFood;
import br.ifrn.edu.sisconf.domain.dtos.OrderFoodResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderFoodSheetRequestDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderFoodMapper {
    @Mapping(target = "id", source = "food.id")
    @Mapping(target = "name", source = "food.name")
    @Mapping(target = "unitPrice", source = "food.unitPrice")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "category", source = "food.category")
    OrderFoodResponseDTO toResponseDTO(OrderFood orderFood);

    @Mapping(source = "food.name", target = "foodName")
    OrderFoodSheetRequestDTO toOrderFoodSheetRequestDTO(OrderFood orderFood);
}
