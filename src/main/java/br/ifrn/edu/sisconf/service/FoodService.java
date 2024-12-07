package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.dtos.FoodCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.FoodResponseDTO;
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

    public FoodResponseDTO createFood(FoodCreateRequestDTO createFoodDto) {
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
}
