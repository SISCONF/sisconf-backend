package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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

import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.Stock;
import br.ifrn.edu.sisconf.domain.StockFood;
import br.ifrn.edu.sisconf.domain.dtos.FoodResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.StockFoodListRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.StockFoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.StockFoodResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.StockResponseDTO;
import br.ifrn.edu.sisconf.domain.enums.FoodCategory;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.StockMapper;
import br.ifrn.edu.sisconf.repository.EntrepreneurRepository;
import br.ifrn.edu.sisconf.repository.FoodRepository;
import br.ifrn.edu.sisconf.repository.StockFoodRepository;
import br.ifrn.edu.sisconf.repository.StockRepository;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {
    @InjectMocks
    private StockService stockService;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @Mock
    private EntrepreneurRepository entrepreneurRepository;

    @Mock
    private PersonService personService;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private StockFoodRepository stockFoodRepository;

    @Test
    public void shouldCreateStockSuccessfully() {
        Entrepreneur entrepreneur = new Entrepreneur();
        entrepreneur.setId(1L);

        stockService.save(entrepreneur);

        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    public void shouldDeleteStockSuccessfully() {
        Long entrepreneurId = 1L;

        Stock stock = new Stock();
        stock.setId(1L);

        when(stockRepository.findByEntrepreneurId(entrepreneurId)).thenReturn(Optional.of(stock));

        stockService.deleteByEntrepreneurId(1L);

        verify(stockRepository).deleteById(stock.getId());
    }

    @Test
    public void shouldThrowErrorWhenDeletingUnexistingEntrepreneurStock() {
        Long entrepreneurId = 1L;

        when(stockRepository.findByEntrepreneurId(entrepreneurId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> stockService.deleteByEntrepreneurId(entrepreneurId));

        assertEquals("Este estoque não existe", exception.getMessage());
    }

    @Test
    public void findEntrepreneurStockSuccessfully() {
        Long entrepreneurId = 1L;
        Entrepreneur entrepreneur = new Entrepreneur();
        entrepreneur.setId(entrepreneurId);

        Stock stock = new Stock();
        stock.setId(1L);
        entrepreneur.setStock(stock);

        StockResponseDTO stockResponseDTO = new StockResponseDTO();
        stockResponseDTO.setId(1L);
        stockResponseDTO.setEntrepreneurId(entrepreneurId);

        Person person = new Person();
        person.setKeycloakId("abcdef");
        String loggedPersonKeycloakId = "abcdef";

        when(entrepreneurRepository.findById(entrepreneurId)).thenReturn(Optional.of(entrepreneur));
        when(stockMapper.toResponseDTO(stock)).thenReturn(stockResponseDTO);

        assertDoesNotThrow(() -> personService.throwIfLoggedPersonIsDifferentFromPersonResource(loggedPersonKeycloakId, person));

        StockResponseDTO stockResult = stockService.getByEntrepreneurId(entrepreneurId, loggedPersonKeycloakId);

        assertEquals(stockResponseDTO, stockResult);
    }

    @Test
    public void failedStockGetBecauseOfUnexistingEntrepreneur() {
        Long entrepreneurId = 1L;
        String loggedPersonKeycloackId = "abcdef";

        when(entrepreneurRepository.findById(entrepreneurId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> stockService.getByEntrepreneurId(entrepreneurId, loggedPersonKeycloackId));

        assertEquals("Empreendedor não encontrado.", exception.getMessage());
    }

    @Test
    public void shouldAssociateFoodToStockSuccessfully() {
        String keycloakId = "user-123";
        
        Entrepreneur entrepreneur = new Entrepreneur();
        entrepreneur.setId(1L);
        
        Food food = new Food();
        food.setId(1L);
        food.setCategory(FoodCategory.FRUIT);
        food.setName("Maçã");
        food.setUnitPrice(BigDecimal.valueOf(12.99));

        StockFoodListRequestDTO stockFoodListRequestDTO = new StockFoodListRequestDTO();
        stockFoodListRequestDTO.setFoodId(food.getId());
        stockFoodListRequestDTO.setQuantity(5);

        StockFoodRequestDTO stockFoodRequestDTO = new StockFoodRequestDTO();
        stockFoodRequestDTO.setFoods(List.of(stockFoodListRequestDTO));
        
        Stock stock = new Stock();
        stock.setId(1L);
        stock.setEntrepreneur(entrepreneur);
        
        StockFood stockFood = new StockFood();
        stockFood.setFood(food);
        stockFood.setId(1L);
        stockFood.setQuantity(3);
        stockFood.setStock(stock);
        
        FoodResponseDTO foodResponseDTO = new FoodResponseDTO();
        foodResponseDTO.setCategory(food.getCategory());
        foodResponseDTO.setId(food.getId());
        foodResponseDTO.setName(food.getName());
        foodResponseDTO.setUnitPrice(food.getUnitPrice());

        StockFoodResponseDTO stockFoodResponseDTO = new StockFoodResponseDTO();
        stockFoodResponseDTO.setQuantity(3);
        stockFoodResponseDTO.setFood(foodResponseDTO);

        StockResponseDTO stockResponseDTO = new StockResponseDTO();
        stockResponseDTO.setId(stock.getId());
        stockResponseDTO.setEntrepreneurId(entrepreneur.getId());
        stockResponseDTO.setStockItems(List.of(stockFoodResponseDTO));

        when(entrepreneurRepository.findById(1L)).thenReturn(Optional.of(entrepreneur));
        when(stockRepository.findByEntrepreneurId(entrepreneur.getId())).thenReturn(Optional.of(stock));
        when(stockMapper.toResponseDTO(stock)).thenReturn(stockResponseDTO);
        when(foodRepository.findAllById(anyList())).thenReturn(List.of(food));

        StockResponseDTO response = stockService.associateFoods(entrepreneur.getId(), stockFoodRequestDTO, keycloakId);

        assertEquals(stockResponseDTO, response);
        verify(entrepreneurRepository).findById(1L);
        verify(stockRepository).findByEntrepreneurId(1L);
    }

    @Test
    public void associatingAlreadyExistingFoodInStockShouldThrownException() {
        String keycloakId = "user-123";

        Entrepreneur entrepreneur = new Entrepreneur();
        entrepreneur.setId(1L);
        
        Stock stock = new Stock();
        stock.setId(1L);
        stock.setEntrepreneur(entrepreneur);
        
        Food food = new Food();
        food.setCategory(FoodCategory.FRUIT);
        food.setId(1L);
        food.setName("Maçã");
        food.setUnitPrice(BigDecimal.valueOf(12.99));
        
        StockFoodListRequestDTO stockFoodListRequestDTO = new StockFoodListRequestDTO();
        stockFoodListRequestDTO.setFoodId(food.getId());
        stockFoodListRequestDTO.setQuantity(5);

        StockFoodRequestDTO stockFoodRequestDTO = new StockFoodRequestDTO();
        stockFoodRequestDTO.setFoods(List.of(stockFoodListRequestDTO));

        StockFood stockFood = new StockFood();
        stockFood.setId(1L);
        stockFood.setFood(food);
        stockFood.setStock(stock);

        stock.setFoods(List.of(stockFood));

        when(entrepreneurRepository.findById(1L)).thenReturn(Optional.of(entrepreneur));
        when(stockRepository.findByEntrepreneurId(entrepreneur.getId())).thenReturn(Optional.of(stock));
        when(foodRepository.findAllById(anyList())).thenReturn(List.of(food));

        assertThrows(BusinessException.class, () -> stockService.associateFoods(entrepreneur.getId(), stockFoodRequestDTO, keycloakId));
    }

    @Test
    public void shouldUpdateStockFoodQuantitySuccessfully() {
        String keycloakId = "user-123";

        Entrepreneur entrepreneur = new Entrepreneur();
        entrepreneur.setId(1L);
        
        Stock stock = new Stock();
        stock.setId(1L);
        stock.setEntrepreneur(entrepreneur);
        
        Food food = new Food();
        food.setCategory(FoodCategory.FRUIT);
        food.setId(1L);
        food.setName("Maçã");
        food.setUnitPrice(BigDecimal.valueOf(12.99));
        
        StockFoodListRequestDTO stockFoodListRequestDTO = new StockFoodListRequestDTO();
        stockFoodListRequestDTO.setFoodId(food.getId());
        stockFoodListRequestDTO.setQuantity(5);

        StockFoodRequestDTO stockFoodRequestDTO = new StockFoodRequestDTO();
        stockFoodRequestDTO.setFoods(List.of(stockFoodListRequestDTO));

        StockFood stockFood = new StockFood();
        stockFood.setId(1L);
        stockFood.setFood(food);
        stockFood.setStock(stock);

        stock.setFoods(List.of(stockFood));

        when(entrepreneurRepository.findById(1L)).thenReturn(Optional.of(entrepreneur));
        when(stockRepository.findByEntrepreneurId(entrepreneur.getId())).thenReturn(Optional.of(stock));
        when(foodRepository.findAllById(anyList())).thenReturn(List.of(food));
        when(stockFoodRepository.findByStockIdAndFoodIdIn(stock.getId(), List.of(food.getId()))).thenReturn(List.of(stockFood));

        stockService.updateStockFoodQuantity(entrepreneur.getId(), stockFoodRequestDTO, keycloakId);

        verify(stockFoodRepository).saveAll(List.of(stockFood));
        verify(entrepreneurRepository).findById(1L);
        verify(stockRepository).findByEntrepreneurId(1L);
    }
}
