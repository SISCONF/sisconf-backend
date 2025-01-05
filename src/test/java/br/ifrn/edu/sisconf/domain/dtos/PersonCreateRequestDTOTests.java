package br.ifrn.edu.sisconf.domain.dtos;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class PersonCreateRequestDTOTests {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreatePersonWhenEmailValidFormat() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setEmail("teste@gmail.com");

        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Email não pode ser vazio")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Email deve seguir o formato de emails")));
    }

    @Test
    public void shouldNotCreatePersonWhenEmailInvalidFormat() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setEmail("teste3");

        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Email deve seguir o formato de emails")));
    }

    @Test
    public void shouldNotCreatePersonWhenEmailNull() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setEmail(null);

        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Email não pode ser vazio")));
    }
}
