package br.ifrn.edu.sisconf.domain.dtos;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class FoodRequestDTOTests {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void foodRequestWithValidUnitPrice() {
        var foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setUnitPrice(new BigDecimal("0.01"));

        Set<ConstraintViolation<FoodRequestDTO>> violations = validator.validate(foodRequestDTO);
        assertFalse(violations.stream().anyMatch(violation -> violation.getMessage().equals("O preço unitário é obrigatório")));
        assertFalse(violations.stream().anyMatch(violation -> violation.getMessage().equals("O preço da comida deve ser maior do que 0")));
    }

    @Test
    public void foodRequestWithInvalidUnitPrice() {
        var foodRequestDTO = new FoodRequestDTO();
        foodRequestDTO.setUnitPrice(new BigDecimal("0.0"));

        Set<ConstraintViolation<FoodRequestDTO>> violations = validator.validate(foodRequestDTO);
        assertFalse(violations.stream().anyMatch(violation -> violation.getMessage().equals("O preço unitário é obrigatório")));
        assertTrue(violations.stream().anyMatch(violation -> violation.getMessage().equals("O preço da comida deve ser maior do que 0")));
    }
}
