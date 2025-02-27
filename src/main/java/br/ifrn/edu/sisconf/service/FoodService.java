package br.ifrn.edu.sisconf.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.dtos.FoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.FoodResponseDTO;
import br.ifrn.edu.sisconf.domain.enums.FoodCategory;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.FoodMapper;
import br.ifrn.edu.sisconf.repository.FoodRepository;
import br.ifrn.edu.sisconf.specification.FoodSpecification;

@Service
public class FoodService {
    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodMapper mapper;

    @Autowired
    private S3Service s3Service;

    public void throwIfFoodAlreadyExists(FoodRequestDTO createFoodDto, Long foodId) {
        if (foodId == null) {
            if (foodRepository.existsByNameAndCategory(createFoodDto.getName(), createFoodDto.getCategory())) {
                throw new BusinessException("Esta comida já está registrada no sistema");
            }
        } else {
            if (foodRepository.existsByNameAndCategoryAndIdNot(createFoodDto.getName(), createFoodDto.getCategory(), foodId)) {
                throw new BusinessException("Esta comida já está registrada no sistema");
            }
        }
    }

    public String generatePresignedUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return "https://sisconf-foods-images-bucket.s3.us-east-2.amazonaws.com/food-placeholder.jpg";
        }

        String key = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        return s3Service.generatePresignedUrl(key);
    }

    public FoodResponseDTO createFood(FoodRequestDTO createFoodDto) {
        throwIfFoodAlreadyExists(createFoodDto, null);
        String foodImageUrl = s3Service.uploadFile(createFoodDto.getImage());
        var food = mapper.toEntity(createFoodDto);
        food.setImageUrl(foodImageUrl);
        foodRepository.save(food);
        return mapper.toResponseDTO(food);
    }

    public List<FoodResponseDTO> listAllFoods(FoodCategory category) {
        Specification<Food> spec = Specification.where(FoodSpecification.ofFoodCategory(category));
        List<Food> foods = foodRepository.findAll(spec);
        List<FoodResponseDTO> foodsWithPresignedURL = foods.stream().map(food -> new FoodResponseDTO(food.getId(), food.getName(), food.getUnitPrice(), food.getCategory(), generatePresignedUrl(food.getImageUrl()))).collect(Collectors.toList());
        return foodsWithPresignedURL;
    }

    public void delete(Long id) {
        if (!foodRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comida não encontrada.");
        }
        foodRepository.deleteById(id);
    }

    public FoodResponseDTO getFood(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comida não encontrada."));
        food.setImageUrl(generatePresignedUrl(food.getImageUrl()));
        return mapper.toResponseDTO(food);

    }

    public FoodResponseDTO update(Long id, FoodRequestDTO foodDto) {
        String newImage = s3Service.uploadFile(foodDto.getImage());

        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comida não econtrada."));
        throwIfFoodAlreadyExists(foodDto, id);

        food.setImageUrl(newImage);
        mapper.updateEntityFromDTO(foodDto, food);
        var updatedFood = foodRepository.save(food);

        return mapper.toResponseDTO(updatedFood);
    }
}
