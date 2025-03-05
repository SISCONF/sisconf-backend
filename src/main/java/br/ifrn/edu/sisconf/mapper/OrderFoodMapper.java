package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.OrderFood;
import br.ifrn.edu.sisconf.domain.dtos.OrderFoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderFoodResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderFoodSheetRequestDTO;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {FoodMapper.class}
)
public interface OrderFoodMapper {
    @Mapping(target = "id", source = "food.id")
    @Mapping(target = "name", source = "food.name")
    @Mapping(target = "unitPrice", source = "food.unitPrice")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "category", source = "food.category")
    OrderFoodResponseDTO toResponseDTO(OrderFood orderFood);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "food.id", source = "foodId")
    void updateEntityFromDTO(
        OrderFoodRequestDTO orderFoodRequestDTO, 
        @MappingTarget OrderFood orderFood
    );

    @Mapping(source = "food.name", target = "foodName")
    OrderFoodSheetRequestDTO toOrderFoodSheetRequestDTO(OrderFood orderFood);
}
