package br.ifrn.edu.sisconf.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ifrn.edu.sisconf.domain.dtos.PersonCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.PersonUpdateRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

public class PersonUpdateRequestDTOTests {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldNotUpdateCreatePersonWhenFirstNameBlank() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setFirstName("");
        
        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome não pode ser vazio")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWhenFirstNameNull() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setFirstName(null);

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome não pode ser vazio")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWhenFirstName256CharsLong() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setFirstName(
            "Maximiliano Bartholomew Alexander Christopher William Ferdinand Jonathan Percival Archibald Montgomery Fitzwilliam III of the House of Strathmore and Kinghorne, Keeper of the Ancient Scrolls, Protector of the Great Seal, Defender of the Realm, Lord of them"
        );

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome deve ter até 255 caracteres")));
    }

    @Test
    public void shouldUpdateCreatePersonWhenFirstName1CharLong() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setFirstName("g");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome deve ter até 255 caracteres")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome não pode ser vazio")));
    }

    @Test
    public void shouldUpdateCreatePersonWithFirstName255CharsLong() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setFirstName("Maximiliano Bartholomew Alexander Christopher William Ferdinand Jonathan Percival Archibald Montgomery Fitzwilliam III of the House of Strathmore and Kinghorne, Keeper of the Ancient Scrolls, Protector of the Great Seal, Defender of the Realm, Lord of the");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome deve ter até 255 caracteres")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome não pode ser vazio")));
    }

    @Test
    public void shouldUpdateCreatePersonWithLastName1CharLong() {
        var createUpdatePersonRequestDTO = new PersonCreateRequestDTO();
        createUpdatePersonRequestDTO.setLastName("k");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome não pode ser vazio")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome deve ter até 255 caracteres")));
    }

    @Test
    public void shouldUpdateCreatePersonWithLastName255CharsLong() {
        var createUpdatePersonRequestDTO = new PersonCreateRequestDTO();
        createUpdatePersonRequestDTO.setLastName(
            "Maximiliano Bartholomew Alexander Christopher" +
            "William Ferdinand Jonathan Percival Archibald Montgomery" +
            "Fitzwilliam III of the House of Strathmore and Kinghorne, " +
            "Keeper of the Ancient Scrolls, Protector of the Great" +
            " Seal, Defender of the Realm, and Lord of t"
        );

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome não pode ser vazio")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome deve ter até 255 caracteres")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithLastNameBlank() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setLastName("");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome não pode ser vazio")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithLastNameNull() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setLastName(null);

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome não pode ser vazio")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithLastName256CharsLong() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setLastName(
            "Maximiliano Bartholomew Alexander Christopher" +
            "William Ferdinand Jonathan Percival Archibald Montgomery" +
            "Fitzwilliam III of the House of Strathmore and Kinghorne, " +
            "Keeper of the Ancient Scrolls, Protector of the Great" +
            " Seal, Defender of the Realm, and Lord of th"
        );

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome deve ter até 255 caracteres")));
    }

    @Test
    public void shouldUpdateCreatePersonWithValidCPFFormat() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setCpf("111.111.111-11");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CPF não pode ser vazio")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CPF deve seguir o formato XXX.XXX.XXX-XX")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithInvalidCPFFormat() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setCpf("23412234");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CPF deve seguir o formato XXX.XXX.XXX-XX")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithCPFContainingNonNumbers() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setCpf("333.abc.345-12");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CPF deve seguir o formato XXX.XXX.XXX-XX")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithNullCPF() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setCpf(null);

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CPF não pode ser vazio")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithBlankCPF() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setCpf("");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CPF não pode ser vazio")));
    }

    @Test
    public void shouldCreateUpdatePersonWithValidCNPJFormat() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setCnpj("11.111.111/1111-11");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CNPJ deve seguir o formato XX.XXX.XXX/XXXX-XX")));
    }

    @Test
    public void shouldNotCreateUpdatePersonWithInvalidCNPJFormat() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setCnpj("112340912");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CNPJ deve seguir o formato XX.XXX.XXX/XXXX-XX")));
    }

    @Test
    public void shouldNotCreateUpdatePersonWithValidCNPJFormatButContainingNonNumbers() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setCnpj("33.as@.456/3333-33");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CNPJ deve seguir o formato XX.XXX.XXX/XXXX-XX")));
    }

    @Test
    public void shouldCreateUpdatePersonWithValidPhone() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setPhone("(11) 91111-1111");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Phone deve seguir formato (XX) XXXXX-XXXX")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Phone não pode ser vazio")));
    }

    @Test
    public void shouldNotCreateUpdatePersonWithInvalidPhoneFormat() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setPhone("1232435");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Phone deve seguir formato (XX) XXXXX-XXXX")));
    }

    @Test
    public void shouldNotCreateUpdatePersonWithValidPhoneFormatButContainingNonNumbers() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setPhone("(22) 9ae31-3333");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Phone deve seguir formato (XX) XXXXX-XXXX")));
    }

    @Test
    public void shouldNotCreateUpdatePersonWhenPhoneNull() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setPhone(null);

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Phone não pode ser vazio")));
    }

    @Test
    public void shouldNotCreateUpdatePersonWhenPhoneBlank() {
        var createUpdatePersonRequestDTO = new PersonUpdateRequestDTO();
        createUpdatePersonRequestDTO.setPhone("");

        Set<ConstraintViolation<PersonUpdateRequestDTO>> violations = validator.validate(createUpdatePersonRequestDTO);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Phone não pode ser vazio")));
    }
}
