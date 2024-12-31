package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ifrn.edu.sisconf.domain.dtos.FoodRequestDTO;
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
    public void throwsErrorForThePriceIsNotGreaterThanZero() {
        BigDecimal unitPrice = new BigDecimal(0);
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setUnitPrice(unitPrice);
        assertThrowsExactly(BusinessException.class, () -> foodService.throwIfInvalidUnitPrice(foodRequestDTO));
    }

    @Test
    public void ErrorNotThrownForThePriceIsGreaterThanZero() {
        BigDecimal unitPrice = new BigDecimal(0.01);
        FoodRequestDTO foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setUnitPrice(unitPrice);
        assertDoesNotThrow(() -> foodService.throwIfInvalidUnitPrice(foodRequestDTO));
    }

    
}
