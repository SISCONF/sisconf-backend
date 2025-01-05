package br.ifrn.edu.sisconf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.dtos.FoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.FoodResponseDTO;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.FoodMapper;
import br.ifrn.edu.sisconf.repository.FoodRepository;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodMapper mapper;

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

    public FoodResponseDTO createFood(FoodRequestDTO createFoodDto) {
        throwIfFoodAlreadyExists(createFoodDto, null);
        var food = mapper.toEntity(createFoodDto);
        foodRepository.save(food);
        return mapper.toResponseDTO(food);
    }

    public List<FoodResponseDTO> listAllFoods() {
        List<Food> foods = foodRepository.findAll();
        return mapper.toDTOList(foods);
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
        return mapper.toResponseDTO(food);

    }

    public FoodResponseDTO update(Long id, FoodRequestDTO foodDto) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comida não econtrada."));
        throwIfFoodAlreadyExists(foodDto, id);
        mapper.updateEntityFromDTO(foodDto, food);
        var updatedFood = foodRepository.save(food);

        return mapper.toResponseDTO(updatedFood);
    }
}
