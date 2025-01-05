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

public class AddressRequestDTOTests {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateUpdateAddressWhenStreet1CharLong() {
        var addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setStreet("c");

        Set<ConstraintViolation<AddressRequestDTO>> violations = validator.validate(addressRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Rua deve ser preenchida")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Rua deve ter no máximo 128 caracteres")));
        
    }

    @Test
    public void shouldCreateUpdateAddressWhenStreet128CharsLong() {
        var addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setStreet(
            "Rua dos Jacarandás " +
            "Floridos do Bairro das " +
            "Águas Claras em " +
            "Comemoração ao Centenário " +
            "da Fundação da Cidade de " +
            "Esperança"
        );

        Set<ConstraintViolation<AddressRequestDTO>> violations = validator.validate(addressRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Rua deve ser preenchida")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Rua deve ter no máximo 128 caracteres")));
    }

    @Test
    public void shouldNotCreatUpdateAddressWhenStreetBlank() {
        var addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setStreet("");

        Set<ConstraintViolation<AddressRequestDTO>> violations = validator.validate(addressRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Rua deve ser preenchida")));
    }

    @Test
    public void shouldNotCreateUpdateAddressWhenStreet129CharsLong() {
        var addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setStreet(
            "Avenida das Magnólias " +
            "Brancas e Ipês Amarelos do " +
            "Parque Ecológico da Serra " +
            "Verde em Homenagem ao " +
            "Bicentenário da " +
            "Independência do Brasil"
        );
        
        Set<ConstraintViolation<AddressRequestDTO>> violations = validator.validate(addressRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Rua deve ter no máximo 128 caracteres")));
    }

    @Test
    public void shouldNotCreateUpdateAddressWhenStreetNull() {
        var addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setStreet(null);

        Set<ConstraintViolation<AddressRequestDTO>> violations = validator.validate(addressRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Rua deve ser preenchida")));
    }

    @Test
    public void shouldCreateUpdateAddressWhenNeighbourhood1CharLong() {
        var addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setNeighbourhood("b");

        Set<ConstraintViolation<AddressRequestDTO>> violations = validator.validate(addressRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Bairro não pode ser vazio")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Bairro deve ter no máximo 128 caracteres")));
    }

    @Test
    public void shouldCreateUpdateWhenNeighbourhood128CharsLong() {
        var addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setNeighbourhood(
            "Residencial das Palmeiras " +
            "Altas e Horizontes " +
            "Verdejantes do Vale " +
            "Encantado em Celebração às " +
            "Belezas Naturais da Região " +
            "Serrana"
        );

        Set<ConstraintViolation<AddressRequestDTO>> violations = validator.validate(addressRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Bairro deve ter no máximo 128 caracteres")));
    }

    @Test
    public void shouldNotCreateUpdateAddressWhenNeighbourhood129CharsLong() {
        var addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setNeighbourhood(
            "Jardim dos Lírios Brancos e " +
            "Rosas Vermelhas do Bosque " +
            "Encantado em Homenagem " +
            "à História Cultural e " +
            "Artística de Nossa CidadeLinda"
        );

        Set<ConstraintViolation<AddressRequestDTO>> violations = validator.validate(addressRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Bairro deve ter no máximo 128 caracteres")));
    }

    @Test
    public void shouldNotCreateUpdateAddressWhenNeighbourhoodBlank() {
        var addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setNeighbourhood("");

        Set<ConstraintViolation<AddressRequestDTO>> violations = validator.validate(addressRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Bairro não pode ser vazio")));
    }

    @Test
    public void shouldNotCreateUpdateAddressWhenNeighbourhoodNull() {
        var addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setNeighbourhood(null);

        Set<ConstraintViolation<AddressRequestDTO>> violations = validator.validate(addressRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Bairro não pode ser vazio")));
    }
}
