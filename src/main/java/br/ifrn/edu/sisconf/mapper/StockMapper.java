package br.ifrn.edu.sisconf.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import br.ifrn.edu.sisconf.domain.Stock;
import br.ifrn.edu.sisconf.domain.dtos.StockResponseDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = StockFoodMapper.class)
public interface StockMapper {
    @Mapping(source = "entrepreneur.id", target = "entrepreneurId")
    StockResponseDTO toResponseDTO(Stock stock);
}
