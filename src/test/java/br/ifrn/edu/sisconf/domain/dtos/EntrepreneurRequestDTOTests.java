package br.ifrn.edu.sisconf.domain.dtos;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class EntrepreneurRequestDTOTests {
    private Validator validator;
    
    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateUpdateEntrepreneurBusinessName128CharsLong() {
        var entrepreneurRequestDTO = new EntrepreneurRequestDTO();
        entrepreneurRequestDTO.setBusinessName(
            "Inovação Global " +
            "Tecnológica e Consultoria " +
            "Avançada para Soluções " +
            "Inteligentes em " +
            "Desenvolvimento " +
            "Sustentável e Tecnologia " +
            "Integr"
        );

        Set<ConstraintViolation<EntrepreneurRequestDTO>> violations = validator.validate(
            entrepreneurRequestDTO
        );

        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "O Nome da Empresa não pode ser vazio"
                )
            )
        );
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "O Nome da Empresa deve conter de 1 a 128 caracteres"
                )
            )
        );
    }

    @Test
    public void shouldCreateUpdateEntrepreneurBusinessName1CharLong() {
        var entrepreneurRequestDTO = new EntrepreneurRequestDTO();
        entrepreneurRequestDTO.setBusinessName("I");

        Set<ConstraintViolation<EntrepreneurRequestDTO>> violations = validator.validate(
            entrepreneurRequestDTO
        );

        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "O Nome da Empresa não pode ser vazio"
                )
            )
        );
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "O Nome da Empresa deve conter de 1 a 128 caracteres"
                )
            )
        );
    }

    @Test
    public void shouldNotCreateUpdateEntrepreneurBusinessNameBlank() {
        var entrepreneurRequestDTO = new EntrepreneurRequestDTO();
        entrepreneurRequestDTO.setBusinessName("");

        Set<ConstraintViolation<EntrepreneurRequestDTO>> violations = validator.validate(
            entrepreneurRequestDTO
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "O Nome da Empresa não pode ser vazio"
                )
            )
        );
    }

    @Test
    public void shouldNotCreateUpdateEntrepreneurBusinessName129CharsLong() {
        var entrepreneurRequestDTO = new EntrepreneurRequestDTO();
        entrepreneurRequestDTO.setBusinessName(
            "Excelência em Soluções " +
            "Avançadas para Consultoria " +
            "e Desenvolvimento " +
            "Sustentável com Inovação e " +
            "Tecnologia de Ponta para um " +
            "Futuro"
        );

        Set<ConstraintViolation<EntrepreneurRequestDTO>> violations = validator.validate(
            entrepreneurRequestDTO
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "O Nome da Empresa deve conter de 1 a 128 caracteres"
                )
            )
        );
    }

    @Test
    public void shouldNotCreateUpdateEntrepreneurBusinessNameNull() {
        var entrepreneurRequestDTO = new EntrepreneurRequestDTO();
        entrepreneurRequestDTO.setBusinessName(null);

        Set<ConstraintViolation<EntrepreneurRequestDTO>> violations = validator.validate(
            entrepreneurRequestDTO
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "O Nome da Empresa não pode ser vazio"
                )
            )
        );
    }
}
