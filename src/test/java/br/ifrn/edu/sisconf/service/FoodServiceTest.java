package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.dtos.FoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.FoodResponseDTO;
import br.ifrn.edu.sisconf.domain.enums.FoodCategory;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.FoodMapper;
import br.ifrn.edu.sisconf.repository.FoodRepository;

@ExtendWith(MockitoExtension.class)
public class FoodServiceTest {
    @InjectMocks
    private FoodService foodService;    

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private FoodMapper foodMapper;

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
    public void duplicateFoodInExistsByNameAndCategoryThrowsError() {
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Maçã");
        foodRequestDTO.setCategory(FoodCategory.FRUIT);
        when(foodRepository.existsByNameAndCategory(foodRequestDTO.getName(), foodRequestDTO.getCategory())).thenReturn(true);

        assertThrowsExactly(BusinessException.class, () -> foodService.throwIfFoodAlreadyExists(foodRequestDTO, null));
    }

    @Test
    public void uniqueFoodInExistsByNameAndCategory() {
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Banana");
        foodRequestDTO.setCategory(FoodCategory.FRUIT);
        when(foodRepository.existsByNameAndCategory(foodRequestDTO.getName(), foodRequestDTO.getCategory())).thenReturn(false);

        assertDoesNotThrow(() -> foodService.throwIfFoodAlreadyExists(foodRequestDTO, null));
    }

    @Test
    public void nonDuplicateFoodInExistsByNameAndCategoryAndIdNotThrowsError() {
        Food food = new Food();
        food.setId(1L);

        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Kiwi");
        foodRequestDTO.setCategory(FoodCategory.FRUIT);

        when(foodRepository.existsByNameAndCategoryAndIdNot(foodRequestDTO.getName(), foodRequestDTO.getCategory(), food.getId())).thenReturn(true);

        assertThrowsExactly(BusinessException.class, () -> foodService.throwIfFoodAlreadyExists(foodRequestDTO, food.getId()));
    }

    @Test
    public void nonDuplicateFoodInExistsByNameAndCategoryAndIdNot() {
        Food food = new Food();
        food.setId(1L);

        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Batata");
        foodRequestDTO.setCategory(FoodCategory.VEGETABLE);

        when(foodRepository.existsByNameAndCategoryAndIdNot(foodRequestDTO.getName(), foodRequestDTO.getCategory(), food.getId())).thenReturn(false);

        assertDoesNotThrow(() -> foodService.throwIfFoodAlreadyExists(foodRequestDTO, food.getId()));
    }

    @Test
    public void createNonDuplicateFoodSuccessfully() {
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Laranja");
        foodRequestDTO.setUnitPrice(new BigDecimal(0.01));
        foodRequestDTO.setCategory(FoodCategory.FRUIT);

        Food food = new Food();
        food.setUnitPrice(new BigDecimal(0.01));
        food.setName("Laranja");
        food.setCategory(FoodCategory.FRUIT);

        FoodResponseDTO foodResponseDTO = new FoodResponseDTO();
        foodResponseDTO.setUnitPrice(new BigDecimal(0.01));
        foodResponseDTO.setName("Laranja");
        foodResponseDTO.setCategory(FoodCategory.FRUIT);

        when(foodRepository.existsByNameAndCategory(foodRequestDTO.getName(), foodRequestDTO.getCategory())).thenReturn(false);
        when(foodMapper.toEntity(foodRequestDTO)).thenReturn(food);
        when(foodMapper.toResponseDTO(food)).thenReturn(foodResponseDTO);

        FoodResponseDTO createdFood = foodService.createFood(foodRequestDTO);

        verify(foodRepository).save(food);
        assertEquals(foodResponseDTO, createdFood);
    }

    @Test
    public void createFoodWithPriceGreaterThanZeroSuccessfully() {
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setUnitPrice(new BigDecimal(0.01));

        Food food = new Food();
        food.setUnitPrice(new BigDecimal(0.01));
        
        FoodResponseDTO foodResponseDTO = new FoodResponseDTO();
        foodResponseDTO.setUnitPrice(new BigDecimal(0.01));

        when(foodRepository.existsByNameAndCategory(foodRequestDTO.getName(), foodRequestDTO.getCategory())).thenReturn(false);
        when(foodMapper.toEntity(foodRequestDTO)).thenReturn(food);
        when(foodMapper.toResponseDTO(food)).thenReturn(foodResponseDTO);

        FoodResponseDTO createdFood = foodService.createFood(foodRequestDTO);

        verify(foodRepository).save(food);
        assertEquals(foodResponseDTO, createdFood);
    }

    @Test
    public void createFoodWithInvalidPriceThrowsError() {
        BigDecimal unitPrice = new BigDecimal(0);
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setUnitPrice(unitPrice);
        Food food = new Food();
        food.setUnitPrice(unitPrice);

        when(foodMapper.toEntity(foodRequestDTO)).thenReturn(food);

        assertThrowsExactly(BusinessException.class, () -> foodService.throwIfInvalidUnitPrice(foodRequestDTO));

        verify(foodRepository, never()).save(food);
    }

    @Test
    public void createDuplicateFoodThrowsError() {
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Laranja");
        foodRequestDTO.setCategory(FoodCategory.FRUIT);

        Food food = new Food();
        food.setName("Laranja");
        food.setCategory(FoodCategory.FRUIT);

        when(foodRepository.existsByNameAndCategory(foodRequestDTO.getName(), foodRequestDTO.getCategory())).thenReturn(true);
        when(foodMapper.toEntity(foodRequestDTO)).thenReturn(food);

        assertThrowsExactly(BusinessException.class, () -> foodService.throwIfFoodAlreadyExists(foodRequestDTO, null));

        verify(foodRepository, never()).save(food);
    }

    @Test
    public void listAllFoods() {
        Food food = new Food();
        List<Food> foods = List.of(food);
        FoodResponseDTO foodResponseDTO = new FoodResponseDTO();
        List<FoodResponseDTO> foodResponseDTOs = List.of(foodResponseDTO);

        when(foodRepository.findAll()).thenReturn(foods);
        when(foodMapper.toDTOList(foods)).thenReturn(foodResponseDTOs);
        
        assertEquals(1, foodResponseDTOs.size());
    }

    @Test
    public void deleteFoodByIdSuccessfully() {
        Food food = new Food();
        food.setId(1L);

        when(foodRepository.existsById(food.getId())).thenReturn(true);
        
        foodService.delete(food.getId());

        verify(foodRepository).deleteById(food.getId());
    }

    @Test
    public void deleteUnexistingFoodThrowsError() {
        Long id = 1L;
        
        assertThrowsExactly(ResourceNotFoundException.class, () -> foodService.delete(id));

        verify(foodRepository, never()).deleteById(id);
    }

    @Test
    public void getFoodWithRequestIdSuccessfully() {
        Food food = new Food();
        food.setId(1L);

        FoodResponseDTO foodResponseDTO = new FoodResponseDTO();
        foodResponseDTO.setId(1L);

        when(foodRepository.findById(food.getId())).thenReturn(Optional.of(food));
        when(foodMapper.toResponseDTO(food)).thenReturn(foodResponseDTO);

        FoodResponseDTO fetchedFood = foodService.getFood(food.getId());

        assertEquals(foodResponseDTO, fetchedFood);
        verify(foodRepository).findById(food.getId());
    }

    @Test
    public void getUnexistingFoodThrowsError() {
        Long id = 1L;

        when(foodRepository.findById(id)).thenReturn(Optional.empty());
        assertThrowsExactly(ResourceNotFoundException.class, () -> foodService.getFood(id));

        verify(foodRepository).findById(id);
    }

    @Test
    public void updateExistingFoodWithNonDuplicateNameAndCategorySuccessfully() {
        Food food = new Food();
        food.setId(1L);
        food.setName("Maçã");
        food.setCategory(FoodCategory.FRUIT);
        food.setUnitPrice(new BigDecimal(0.01));

        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Cenoura");
        foodRequestDTO.setCategory(FoodCategory.VEGETABLE);
        foodRequestDTO.setUnitPrice(new BigDecimal(0.01));

        FoodResponseDTO foodResponseDTO = new FoodResponseDTO();
        foodResponseDTO.setId(1L);
        foodResponseDTO.setName("Cenoura");
        foodResponseDTO.setCategory(FoodCategory.VEGETABLE);
        foodResponseDTO.setUnitPrice(new BigDecimal(0.01));

        when(foodRepository.findById(food.getId())).thenReturn(Optional.of(food));
        when(foodRepository.existsByNameAndCategoryAndIdNot(foodRequestDTO.getName(), foodRequestDTO.getCategory(), food.getId())).thenReturn(false);
        when(foodMapper.toResponseDTO(food)).thenReturn(foodResponseDTO);
        when(foodRepository.save(food)).thenReturn(food);

        FoodResponseDTO updatedFood = foodService.update(food.getId(), foodRequestDTO);
        
        verify(foodMapper).updateEntityFromDTO(foodRequestDTO, food);
        verify(foodRepository).save(food);
        assertEquals(foodResponseDTO, updatedFood);
    }

    @Test
    public void updateExistingFoodWithValidUnitPriceSuccessfully() {
        Food food = new Food();
        food.setId(1L);
        food.setUnitPrice(new BigDecimal(0.01));

        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setUnitPrice(new BigDecimal(0.01));

        FoodResponseDTO foodResponseDTO = new FoodResponseDTO();
        foodResponseDTO.setId(1L);
        foodResponseDTO.setUnitPrice(new BigDecimal(0.01));

        when(foodRepository.findById(food.getId())).thenReturn(Optional.of(food));
        when(foodRepository.existsByNameAndCategoryAndIdNot(foodRequestDTO.getName(), foodRequestDTO.getCategory(), food.getId())).thenReturn(false);
        when(foodMapper.toResponseDTO(food)).thenReturn(foodResponseDTO);
        when(foodRepository.save(food)).thenReturn(food);

        FoodResponseDTO updatedFood = foodService.update(food.getId(), foodRequestDTO);
        
        verify(foodMapper).updateEntityFromDTO(foodRequestDTO, food);
        verify(foodRepository).save(food);
        assertEquals(foodResponseDTO, updatedFood);
    }

    @Test
    public void updateFoodWithNonUniqueNameInCategoryThrowsError() {
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Cebola");
        foodRequestDTO.setCategory(FoodCategory.VEGETABLE);

        Food food = new Food();
        food.setId(1L);
        food.setName("Maçã");
        food.setCategory(FoodCategory.FRUIT);

        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));
        when(foodRepository.existsByNameAndCategoryAndIdNot(foodRequestDTO.getName(), foodRequestDTO.getCategory(), 1L)).thenReturn(true);

        assertThrowsExactly(BusinessException.class, () -> foodService.throwIfFoodAlreadyExists(foodRequestDTO, 1L));

        verify(foodMapper, never()).updateEntityFromDTO(foodRequestDTO, food);
        verify(foodRepository, never()).save(food);
    }

    @Test
    public void updateFoodWithInvalidUnitPriceThrowsError() {
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setUnitPrice(new BigDecimal(0));

        Food food = new Food();
        food.setId(1L);
        food.setUnitPrice(new BigDecimal(0.01));    

        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));
        when(foodRepository.existsByNameAndCategoryAndIdNot(foodRequestDTO.getName(), foodRequestDTO.getCategory(), 1L)).thenReturn(false);

        assertThrowsExactly(BusinessException.class, () -> foodService.throwIfInvalidUnitPrice(foodRequestDTO));
        
        verify(foodMapper, never()).updateEntityFromDTO(foodRequestDTO, food);
        verify(foodRepository, never()).save(food);
    }

    @Test
    public void updateUnexistingFoodThrowsError() {
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setName("Abacaxi");
        foodRequestDTO.setCategory(FoodCategory.FRUIT);
        foodRequestDTO.setUnitPrice(new BigDecimal(0.01));

        when(foodRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrowsExactly(ResourceNotFoundException.class, () -> foodService.update(1L, foodRequestDTO));

        verify(foodRepository, never()).save(any(Food.class));
    }
}