package br.ifrn.edu.sisconf.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ifrn.edu.sisconf.domain.dtos.PersonCreateRequestDTO;
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

    @Test
    public void shouldCreatePersonWhenPassword1Valid() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPassword("abcd1234");
        
        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Senha não pode ser vazia")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Senha deve ter de 8 a 16 caracteres")));
    }

    @Test
    public void shouldNotCreatePersonWhenPassword1Blank() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPassword("");
        
        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Senha não pode ser vazia")));
    }

    @Test
    public void shouldNotCreatePersonWhenPassword1Is7CharsLong() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPassword("abc1234");
        
        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Senha deve ter de 8 a 16 caracteres")));
    }

    @Test
    public void shouldNotCreatePersonWhenPassword1Is17CharsLong() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPassword("abcdefgh123456789");
        
        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Senha deve ter de 8 a 16 caracteres")));
    }

    @Test
    public void shouldCreatePersonWhenPassword1Is16CharsLong() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPassword("abcdefgh12345678");
        
        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Senha deve ter de 8 a 16 caracteres")));
    }

    @Test
    public void shouldNotCreatePersonWhenPassword1IsNull() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPassword(null);

        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Senha não pode ser vazia")));
    }

    @Test
    public void shouldCreatePersonWhenPassword2Valid() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPassword2("abcd1234");
        
        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Confirmar senha não pode ser vazia")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Confirmar senha deve ter de 8 a 16 caracteres")));
    }

    @Test
    public void shouldNotCreatePersonWhenPassword2Blank() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPassword2("");
        
        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Confirmar senha não pode ser vazia")));
    }

    @Test
    public void shouldNotCreatePersonWhenPassword2Is7CharsLong() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPassword2("abc1234");
        
        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Confirmar senha deve ter de 8 a 16 caracteres")));
    }

    @Test
    public void shouldNotCreatePersonWhenPassword2Is17CharsLong() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPassword2("abcdefgh123456789");
        
        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Confirmar senha deve ter de 8 a 16 caracteres")));
    }

    @Test
    public void shouldCreatePersonWhenPassword2Is16CharsLong() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPassword2("abcdefgh12345678");
        
        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Confirmar senha deve ter de 8 a 16 caracteres")));
    }

    @Test
    public void shouldNotCreatePersonWhenPassword2IsNull() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPassword2(null);

        Set<ConstraintViolation<PersonCreateRequestDTO>> violations = validator.validate(personCreateRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Confirmar senha não pode ser vazia")));
    }
}
