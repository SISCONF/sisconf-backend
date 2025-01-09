package br.ifrn.edu.sisconf.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerRequestDTO;
import br.ifrn.edu.sisconf.util.CustomerTestUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class CustomerRequestDTOTests {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void sholdNotCreateUpdateCustomerWhenCategoryNull() {
        var customerCreateRequestDTO = CustomerTestUtil.createValidCustomerCreateRequestDTO();
        customerCreateRequestDTO.setCategory(null);

        Set<ConstraintViolation<CustomerRequestDTO>> violations = validator.validate(
            customerCreateRequestDTO
        );
        
        assertEquals(1, violations.stream().toList().size());
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Categoria não pode ser vazio"
            )));
    }

    @Test
    public void shouldCreateUpdateCustomerWhenCategoryValid() {
        var customerCreateRequestDTO = CustomerTestUtil.createValidCustomerCreateRequestDTO();
        Set<ConstraintViolation<CustomerRequestDTO>> violations = validator.validate(
            customerCreateRequestDTO
        );
        
        assertEquals(0, violations.stream().toList().size());
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Categoria não pode ser vazio"
            )));
    }
}
