package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.dtos.FoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.FoodResponseDTO;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.FoodMapper;
import br.ifrn.edu.sisconf.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodMapper mapper;

    public FoodResponseDTO createFood(FoodRequestDTO createFoodDto) {
        if (createFoodDto.getUnit_price() != null && createFoodDto.getUnit_price().signum() != 1) {
            throw new BusinessException("O preço unitário não pode ser 0, nem negativo.");
        }
        if (createFoodDto.getName() != null && createFoodDto.getName().isEmpty()) {
            throw new BusinessException("O campo nome não pode ser um texto vazio.");
        }
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
        if (foodDto.getUnit_price() != null && foodDto.getUnit_price().signum() != 1) {
            throw new BusinessException("O preço unitário não pode ser 0, nem negativo.");
        }
        if (foodDto.getName() != null && foodDto.getName().isEmpty()) {
            throw new BusinessException("O campo nome não pode ser um texto vazio.");
        }
        mapper.updateEntityFromDTO(foodDto, food);
        var updatedFood = foodRepository.save(food);

        return mapper.toResponseDTO(updatedFood);
    }
}
