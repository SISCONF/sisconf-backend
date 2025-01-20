package br.ifrn.edu.sisconf.domain.dtos;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ifrn.edu.sisconf.domain.dtos.Order.OrderRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class OrderRequestDTOTests {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void orderRequestWithValidFoodQuantities() {
        var orderFoodRequestDTO = new OrderFoodRequestDTO(1L, 2);
        var orderRequestDTO = new OrderRequestDTO(List.of(orderFoodRequestDTO));

        Set<ConstraintViolation<OrderRequestDTO>> violations = validator.validate(orderRequestDTO);
        assertFalse(violations.stream().anyMatch(violation -> violation.getMessage().equals("Um pedido tem que ter pelo menos 1 comida")));
    }

    @Test
    public void orderRequestWithEmptyFoodList() {
        var orderRequestDTO = new OrderRequestDTO(List.of());

        Set<ConstraintViolation<OrderRequestDTO>> violations = validator.validate(orderRequestDTO);
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("Um pedido tem que ter pelo menos 1 comida")));
    }

    
}
