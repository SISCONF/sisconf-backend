package br.ifrn.edu.sisconf.domain.dtos;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ifrn.edu.sisconf.domain.dtos.Person.CreatePersonGroup;
import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.UpdatePersonGroup;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.groups.Default;

public class PersonRequestDTOTests {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreatePersonWhenEmailValidFormat() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setEmail("teste@gmail.com");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO, 
            CreatePersonGroup.class, Default.class
        );

        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Email não pode ser vazio"
                )
            )
        );
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Email deve seguir o formato de emails"
                )
            )
        );
    }

    @Test
    public void shouldNotCreatePersonWhenEmailInvalidFormat() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setEmail("teste3");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO, 
            CreatePersonGroup.class, Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Email deve seguir o formato de emails"
                )
            )
        );
    }

    @Test
    public void shouldNotCreatePersonWhenEmailNull() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setEmail(null);

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class, Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Email não pode ser vazio")));
    }

    @Test
    public void shouldCreatePersonWhenPassword1Valid() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPassword("abcd1234");
        
        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class, Default.class
        );

        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Senha não pode ser vazia")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Senha deve ter de 8 a 16 caracteres"
                )
            )
        );
    }

    @Test
    public void shouldNotCreatePersonWhenPassword1Blank() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPassword("");
        
        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class, Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Senha não pode ser vazia")));
    }

    @Test
    public void shouldNotCreatePersonWhenPassword1Is7CharsLong() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPassword("abc1234");
        
        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class, Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Senha deve ter de 8 a 16 caracteres"
                )
            )
        );
    }

    @Test
    public void shouldNotCreatePersonWhenPassword1Is17CharsLong() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPassword("abcdefgh123456789");
        
        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class, Default.class
        );
        
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Senha deve ter de 8 a 16 caracteres"
                )
            )
        );
    }

    @Test
    public void shouldCreatePersonWhenPassword1Is16CharsLong() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPassword("abcdefgh12345678");
        
        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class, Default.class
        );

        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Senha deve ter de 8 a 16 caracteres"
                )
            )
        );
    }

    @Test
    public void shouldNotCreatePersonWhenPassword1IsNull() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPassword(null);

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class, Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Senha não pode ser vazia")));
    }

    @Test
    public void shouldCreatePersonWhenPassword2Valid() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPassword2("abcd1234");
        
        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class, Default.class
        );

        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Confirmar senha não pode ser vazia"
                )
            )
        );
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Confirmar senha deve ter de 8 a 16 caracteres"
                )
            )
        );
    }

    @Test
    public void shouldNotCreatePersonWhenPassword2Blank() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPassword2("");
        
        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class, Default.class
        );
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Confirmar senha não pode ser vazia"
                )
            )
        );
    }

    @Test
    public void shouldNotCreatePersonWhenPassword2Is7CharsLong() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPassword2("abc1234");
        
        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class, Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Confirmar senha deve ter de 8 a 16 caracteres"
                )
            )
        );
    }

    @Test
    public void shouldNotCreatePersonWhenPassword2Is17CharsLong() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPassword2("abcdefgh123456789");
        
        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class, Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Confirmar senha deve ter de 8 a 16 caracteres"
                )
            )
        );
    }

    @Test
    public void shouldCreatePersonWhenPassword2Is16CharsLong() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPassword2("abcdefgh12345678");
        
        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class, Default.class
        );
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Confirmar senha deve ter de 8 a 16 caracteres"
                )
            )
        );
    }

    @Test
    public void shouldNotCreatePersonWhenPassword2IsNull() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPassword2(null);

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class, Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals(
                "Confirmar senha não pode ser vazia"
                )
            )
        );
    }

    @Test
    public void shouldNotUpdateCreatePersonWhenFirstNameBlank() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setFirstName("");
        
        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome não pode ser vazio")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWhenFirstNameNull() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setFirstName(null);

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome não pode ser vazio")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWhenFirstName256CharsLong() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setFirstName(
            "Maximiliano Bartholomew Alexander Christopher" +
            " William Ferdinand Jonathan Percival Archibald Montgomery" +
            " Fitzwilliam III of the House of Strathmore and Kinghorne," +
            " Keeper of the Ancient Scrolls, Protector of the Great Seal," +
            " Defender of the Realm, Lord of them"
        );

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome deve ter até 255 caracteres")));
    }

    @Test
    public void shouldUpdateCreatePersonWhenFirstName1CharLong() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setFirstName("g");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome deve ter até 255 caracteres")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome não pode ser vazio")));
    }

    @Test
    public void shouldUpdateCreatePersonWithFirstName255CharsLong() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setFirstName(
            "Maximiliano Bartholomew Alexander Christopher William" +
            " Ferdinand Jonathan Percival Archibald Montgomery Fitzwilliam III" +
            " of the House of Strathmore and Kinghorne, Keeper of the Ancient Scrolls" +
            ", Protector of the Great Seal, Defender of the Realm, Lord of the"
        );

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome deve ter até 255 caracteres")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Nome não pode ser vazio")));
    }

    @Test
    public void shouldUpdateCreatePersonWithLastName1CharLong() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setLastName("k");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome não pode ser vazio")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome deve ter até 255 caracteres")));
    }

    @Test
    public void shouldUpdateCreatePersonWithLastName255CharsLong() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setLastName(
            "Maximiliano Bartholomew Alexander Christopher" +
            "William Ferdinand Jonathan Percival Archibald Montgomery" +
            "Fitzwilliam III of the House of Strathmore and Kinghorne, " +
            "Keeper of the Ancient Scrolls, Protector of the Great" +
            " Seal, Defender of the Realm, and Lord of t"
        );

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome não pode ser vazio")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome deve ter até 255 caracteres")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithLastNameBlank() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setLastName("");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome não pode ser vazio")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithLastNameNull() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setLastName(null);

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome não pode ser vazio")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithLastName256CharsLong() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setLastName(
            "Maximiliano Bartholomew Alexander Christopher" +
            "William Ferdinand Jonathan Percival Archibald Montgomery" +
            "Fitzwilliam III of the House of Strathmore and Kinghorne, " +
            "Keeper of the Ancient Scrolls, Protector of the Great" +
            " Seal, Defender of the Realm, and Lord of th"
        );

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Sobrenome deve ter até 255 caracteres")));
    }

    @Test
    public void shouldUpdateCreatePersonWithValidCPFFormat() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCpf("111.111.111-11");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CPF não pode ser vazio")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CPF deve seguir o formato XXX.XXX.XXX-XX")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithInvalidCPFFormat() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCpf("23412234");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CPF deve seguir o formato XXX.XXX.XXX-XX")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithCPFContainingNonNumbers() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCpf("333.abc.345-12");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CPF deve seguir o formato XXX.XXX.XXX-XX")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithNullCPF() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCpf(null);

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CPF não pode ser vazio")));
    }

    @Test
    public void shouldNotUpdateCreatePersonWithBlankCPF() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCpf("");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CPF não pode ser vazio")));
    }

    @Test
    public void shouldCreateUpdatePersonWithValidCNPJFormat() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCnpj("11.111.111/1111-11");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CNPJ deve seguir o formato XX.XXX.XXX/XXXX-XX")));
    }

    @Test
    public void shouldNotCreateUpdatePersonWithInvalidCNPJFormat() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCnpj("112340912");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CNPJ deve seguir o formato XX.XXX.XXX/XXXX-XX")));
    }

    @Test
    public void shouldNotCreateUpdatePersonWithValidCNPJFormatButContainingNonNumbers() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCnpj("33.as@.456/3333-33");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("CNPJ deve seguir o formato XX.XXX.XXX/XXXX-XX")));
    }

    @Test
    public void shouldCreateUpdatePersonWithValidPhone() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPhone("(11) 91111-1111");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Telefone deve seguir formato (XX) XXXXX-XXXX")));
        assertFalse(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Telefone não pode ser vazio")));
    }

    @Test
    public void shouldNotCreateUpdatePersonWithInvalidPhoneFormat() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPhone("1232435");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Telefone deve seguir formato (XX) XXXXX-XXXX")));
    }

    @Test
    public void shouldNotCreateUpdatePersonWithValidPhoneFormatButContainingNonNumbers() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPhone("(22) 9ae31-3333");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );

        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Telefone deve seguir formato (XX) XXXXX-XXXX")));
    }

    @Test
    public void shouldNotCreateUpdatePersonWhenPhoneNull() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPhone(null);

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Telefone não pode ser vazio")));
    }

    @Test
    public void shouldNotCreateUpdatePersonWhenPhoneBlank() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPhone("");

        Set<ConstraintViolation<PersonRequestDTO>> violations = validator.validate(
            personRequestDTO,
            CreatePersonGroup.class,
            UpdatePersonGroup.class,
            Default.class
        );
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().equals("Telefone não pode ser vazio")));
    }
}