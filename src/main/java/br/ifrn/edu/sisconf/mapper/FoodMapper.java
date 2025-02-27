package br.ifrn.edu.sisconf.mapper;

import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.dtos.FoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.FoodResponseDTO;
import br.ifrn.edu.sisconf.service.S3Service;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class FoodMapper {
    @Autowired
    private S3Service s3Service;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "stocks", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    public abstract Food toEntity(FoodRequestDTO createFoodDto);

    public abstract FoodResponseDTO toResponseDTO(Food food);

    public abstract List<FoodResponseDTO> toDTOList(List<Food> foods);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "stocks", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    public abstract void updateEntityFromDTO(FoodRequestDTO dto, @MappingTarget Food food);

    @AfterMapping
    protected void setPresignedUrl(@MappingTarget FoodResponseDTO foodResponseDTO, Food food) {
        String foodImageUrl = food.getImageUrl();
        if (foodImageUrl != null) {
            String key = foodImageUrl.substring(foodImageUrl.lastIndexOf("/") + 1);
            String presignedUrl = s3Service.generatePresignedUrl(key);
            foodResponseDTO.setImageUrl(presignedUrl);
            return;
        }
        food.setImageUrl("https://sisconf-foods-images-bucket.s3.us-east-2.amazonaws.com/food-placeholder.jpg");
    }
}
