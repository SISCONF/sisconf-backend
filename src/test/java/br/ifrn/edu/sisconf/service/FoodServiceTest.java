package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.dtos.FoodRequestDTO;
import br.ifrn.edu.sisconf.domain.enums.FoodCategory;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.mapper.FoodMapper;
import br.ifrn.edu.sisconf.repository.FoodRepository;

public class FoodServiceTest {
    @InjectMocks
    private FoodService foodService;    

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private FoodMapper foodMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void foodPriceIsNotGreaterThanZero() {
        BigDecimal unitPrice = new BigDecimal(0);
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setUnitPrice(unitPrice);
        assertThrowsExactly(BusinessException.class, () -> foodService.throwIfInvalidUnitPrice(foodRequestDTO));
    }

    @Test
    public void foodPriceIsGreaterThanZero() {
        BigDecimal unitPrice = new BigDecimal(0.01);
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setUnitPrice(unitPrice);
        assertDoesNotThrow(() -> foodService.throwIfInvalidUnitPrice(foodRequestDTO));
    }

    @Test
    public void creatingFoodWithNonUniqueNameAndCategory() {
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Maçã");
        foodRequestDTO.setCategory(FoodCategory.FRUIT);
        when(foodRepository.existsByNameAndCategory(foodRequestDTO.getName(), foodRequestDTO.getCategory())).thenReturn(true);

        assertThrowsExactly(BusinessException.class, () -> foodService.throwIfFoodAlreadyExists(foodRequestDTO, null));
    }

    @Test
    public void creatingFoodWithUniqueNameAndCategory() {
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Banana");
        foodRequestDTO.setCategory(FoodCategory.FRUIT);
        when(foodRepository.existsByNameAndCategory(foodRequestDTO.getName(), foodRequestDTO.getCategory())).thenReturn(false);

        assertDoesNotThrow(() -> foodService.throwIfFoodAlreadyExists(foodRequestDTO, null));
    }

    @Test
    public void updatingFoodWithNonUniqueNameAndCategory() {
        Food food = new Food();
        food.setId(1L);

        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Kiwi");
        foodRequestDTO.setCategory(FoodCategory.FRUIT);

        when(foodRepository.existsByNameAndCategoryAndIdNot(foodRequestDTO.getName(), foodRequestDTO.getCategory(), food.getId())).thenReturn(true);

        assertThrowsExactly(BusinessException.class, () -> foodService.throwIfFoodAlreadyExists(foodRequestDTO, food.getId()));
    }

    @Test
    public void updatingFoodWithUniqueNameAndCategory() {
        Food food = new Food();
        food.setId(1L);

        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Batata");
        foodRequestDTO.setCategory(FoodCategory.VEGETABLE);

        when(foodRepository.existsByNameAndCategoryAndIdNot(foodRequestDTO.getName(), foodRequestDTO.getCategory(), food.getId())).thenReturn(false);

        assertDoesNotThrow(() -> foodService.throwIfFoodAlreadyExists(foodRequestDTO, food.getId()));
    }
}
