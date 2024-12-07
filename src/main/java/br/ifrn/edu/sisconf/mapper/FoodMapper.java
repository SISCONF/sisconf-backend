package br.ifrn.edu.sisconf.mapper;
import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.dtos.FoodCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.FoodResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FoodMapper {
    Food toEntity(FoodCreateRequestDTO createFoodDto);

    FoodResponseDTO toResponseDTO(Food food);

    List<FoodResponseDTO> toDTOList(List<Food> foods);
}
