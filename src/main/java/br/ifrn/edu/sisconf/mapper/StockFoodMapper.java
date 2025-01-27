package br.ifrn.edu.sisconf.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import br.ifrn.edu.sisconf.domain.StockFood;
import br.ifrn.edu.sisconf.domain.dtos.StockFoodRequestDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StockFoodMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "food.id", source = "foodId")
    @Mapping(target = "stock.id", source = "stockId")
    StockFood toEntity(StockFoodRequestDTO stockFoodRequestDTO);
}
