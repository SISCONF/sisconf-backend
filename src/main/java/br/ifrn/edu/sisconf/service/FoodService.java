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

    public void throwIfInvalidUnitPrice(FoodRequestDTO createFoodDto) {
        if (createFoodDto.getUnitPrice() != null && createFoodDto.getUnitPrice().signum() != 1) {
            throw new BusinessException("O preço unitário não pode ser 0, nem negativo.");
        }
    }

    public void throwIfInvalidName(FoodRequestDTO createFoodDto) {
        if (createFoodDto.getName() != null && createFoodDto.getName().isEmpty()) {
            throw new BusinessException("O campo nome não pode ser um texto vazio.");
        }
    }

    public FoodResponseDTO createFood(FoodRequestDTO createFoodDto) {
        throwIfInvalidUnitPrice(createFoodDto);
        throwIfInvalidName(createFoodDto);
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
        throwIfInvalidUnitPrice(foodDto);
        throwIfInvalidName(foodDto);
        mapper.updateEntityFromDTO(foodDto, food);
        var updatedFood = foodRepository.save(food);

        return mapper.toResponseDTO(updatedFood);
    }
}
