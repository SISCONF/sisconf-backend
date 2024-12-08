package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.dtos.FoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.FoodResponseDTO;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.FoodMapper;
import br.ifrn.edu.sisconf.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodMapper mapper;

    public FoodResponseDTO createFood(FoodRequestDTO createFoodDto) {
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
            throw new ResourceNotFoundException("Comida não encontrada");
        }
        foodRepository.deleteById(id);
    }

    public FoodResponseDTO getFood(Long id) {
        Optional<Food> foundFood = foodRepository.findById(id);
        Food food = foundFood.get();
        if (food == null) {
            throw new ResourceNotFoundException("Esta comida não está registrada no banco");
        }
        return mapper.toResponseDTO(food);

    }

    public FoodResponseDTO update(Long id, FoodRequestDTO foodDto) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comida não econtrada."));
        mapper.updateEntityFromDTO(foodDto, food);
        var updatedFood = foodRepository.save(food);

        return mapper.toResponseDTO(updatedFood);
    }
}
