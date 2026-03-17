package br.ifrn.edu.sisconf.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import br.ifrn.edu.sisconf.domain.StockFood;
import br.ifrn.edu.sisconf.domain.dtos.StockFoodResponseDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StockFoodMapper {
    StockFoodResponseDTO toResponseDTO(StockFood stockFood);
}
