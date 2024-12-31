package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ifrn.edu.sisconf.domain.Address;
import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.domain.CountryState;
import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.dtos.PersonCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.PersonUpdateRequestDTO;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.mapper.PersonMapper;
import br.ifrn.edu.sisconf.repository.PersonRepository;

public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;
    
    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPersonWhileCPFUnused() {
        PersonCreateRequestDTO personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setCpf("123.456.789-10");
        when(personRepository.existsByCpf(personCreateRequestDTO.getCpf())).thenReturn(false);
        
        assertDoesNotThrow(() -> personService.throwIfCpfIsNotUnique(personCreateRequestDTO, null));
    }

    @Test
    void createPersonWhileCPFUsed() {
        PersonCreateRequestDTO personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setCpf("123.456.789-10");

        when(personRepository.existsByCpf(personCreateRequestDTO.getCpf())).thenReturn(true);

        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfCpfIsNotUnique(personCreateRequestDTO, null)
        );
    }

    @Test
    void updatePersonWhileCPFUnused() {
        var countryState = new CountryState("Pernambuco", "PE");
        countryState.setId(1L);
        var city = new City("Petrolina", countryState);
        city.setId(1L);
        var address = new Address(
            "Rua Teste",
            "10000-000",
            "Teste",
            10,
            city
        );
        address.setId(1L);
        var personToBeUpdated = new Person(
            "asdasd019201",
            "Antônio",
            "Carlos",
            "antonio.carlos@gmail.com",
            "111.111.111-34",
            null,
            "(22) 22222-2222",
            address
        );
        personToBeUpdated.setId(1L);
        var personUpdateRequestDTO = new PersonUpdateRequestDTO();
        personUpdateRequestDTO.setCpf("123.456.789-10");
        when(personRepository.existsByCpfAndIdNot(personUpdateRequestDTO.getCpf(), personToBeUpdated.getId())).thenReturn(false);

        assertDoesNotThrow(() -> personService.throwIfCpfIsNotUnique(personUpdateRequestDTO, personToBeUpdated.getId()));
    }

    @Test
    void updatePersonWhileCPFUsed() {
        var countryState = new CountryState("Pernambuco", "PE");
        countryState.setId(1L);
        var city = new City("Petrolina", countryState);
        city.setId(1L);
        var address = new Address(
            "Rua Teste",
            "10000-000",
            "Teste",
            10,
            city
        );
        address.setId(1L);
        var personToBeUpdated = new Person(
            "asdasd019201",
            "Antônio",
            "Carlos",
            "antonio.carlos@gmail.com",
            "111.111.111-34",
            null,
            "(22) 22222-2222",
            address
        );
        personToBeUpdated.setId(1L);
        var personUpdateRequestDTO = new PersonUpdateRequestDTO();
        personUpdateRequestDTO.setCpf("123.456.789-10");
        when(personRepository.existsByCpfAndIdNot(personUpdateRequestDTO.getCpf(), personToBeUpdated.getId())).thenReturn(true);

        assertThrowsExactly(
            BusinessException.class, 
            () -> personService.throwIfCpfIsNotUnique(personUpdateRequestDTO, personToBeUpdated.getId())
        );
    }

    @Test
    void createPersonIfPasswordsMatch() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        final String password = "abc12345";
        personCreateRequestDTO.setPassword(password);
        personCreateRequestDTO.setPassword2(password);

        assertDoesNotThrow(() -> personService.throwIfPasswordsDontMatch(personCreateRequestDTO));
    }

    @Test
    void dontCreatePersonIfPasswordsMatch() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPassword("abc123456");
        personCreateRequestDTO.setPassword2("abc12345");

        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfPasswordsDontMatch(personCreateRequestDTO)
        );
    }

    @Test
    void dontCreatePersonIfEmailIsNotUnique() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setEmail("teste2@email.com");
        when(personRepository.existsByEmail(personCreateRequestDTO.getEmail())).thenReturn(true);

        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfEmailIsNotUnique(personCreateRequestDTO)
        );
    }

    @Test
    void createPersonIfEmailIsUnique() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setEmail("teste@email.com");
        when(personRepository.existsByEmail(personCreateRequestDTO.getEmail())).thenReturn(false);

        assertDoesNotThrow(() -> personService.throwIfEmailIsNotUnique(personCreateRequestDTO));
    }

    @Test
    void dontCreatePersonIfCnpjIsNotUnique() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setCnpj("22.222.222/0001-22");
        when(personRepository.existsByCnpj(personCreateRequestDTO.getCnpj())).thenReturn(true);

        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfCnpjIsNotUnique(personCreateRequestDTO, null)
        );
    }

    @Test
    void createPersonIfCnpjIsUnique() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setCnpj("11.111.111/0001-11");
        when(personRepository.existsByCnpj(personCreateRequestDTO.getCnpj())).thenReturn(false);

        assertDoesNotThrow(() -> personService.throwIfCnpjIsNotUnique(personCreateRequestDTO, null));
    }

    @Test
    void dontUpdatePersonIfCnpjIsNotUnique() {
        var countryState = new CountryState("Pernambuco", "PE");
        countryState.setId(1L);
        var city = new City("Petrolina", countryState);
        city.setId(1L);
        var address = new Address(
            "Rua Teste",
            "10000-000",
            "Teste",
            10,
            city
        );
        address.setId(1L);
        var personToBeUpdated = new Person(
            "asdasd019201",
            "Lôbo",
            "Solitário",
            "lobo.solitario@gmail.com",
            "111.111.111-34",
            "33.333.333/0001-33",
            "(22) 22222-2222",
            address
        );
        personToBeUpdated.setId(1L);
        var personUpdateRequestDTO = new PersonUpdateRequestDTO();
        personUpdateRequestDTO.setCnpj("22.222.222/0001-22");
        when(personRepository.existsByCnpjAndIdNot(personUpdateRequestDTO.getCnpj(), personToBeUpdated.getId())).thenReturn(true);

        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfCnpjIsNotUnique(personUpdateRequestDTO, personToBeUpdated.getId())
        );
    }

    @Test
    void updatePersonIfCnpjIsUnique() {
        var countryState = new CountryState("Pernambuco", "PE");
        countryState.setId(1L);
        var city = new City("Petrolina", countryState);
        city.setId(1L);
        var address = new Address(
            "Rua Teste",
            "10000-000",
            "Teste",
            10,
            city
        );
        address.setId(1L);
        var personToBeUpdated = new Person(
            "asdasd019201",
            "Lôbo",
            "Solitário",
            "lobo.solitario@gmail.com",
            "111.111.111-34",
            "33.333.333/0001-33",
            "(22) 22222-2222",
            address
        );
        personToBeUpdated.setId(1L);
        var personUpdateRequestDTO = new PersonUpdateRequestDTO();
        personUpdateRequestDTO.setCnpj("11.111.111/0001-11");
        when(personRepository.existsByCnpjAndIdNot(personUpdateRequestDTO.getCnpj(), personToBeUpdated.getId())).thenReturn(false);

        assertDoesNotThrow(() -> personService.throwIfCnpjIsNotUnique(personUpdateRequestDTO, personToBeUpdated.getId()));
    }

    @Test
    void createPersonIfPhoneIsUnique() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPhone("(11) 91111-1111");
        when(personRepository.existsByPhone(personCreateRequestDTO.getPhone())).thenReturn(false);

        assertDoesNotThrow(() -> personService.throwIfPhoneIsNotUnique(
            personCreateRequestDTO,
            null
        ));
    }

    @Test
    void dontCreatePersonIfPhoneIsNotUnique() {
        var personCreateRequestDTO = new PersonCreateRequestDTO();
        personCreateRequestDTO.setPhone("(11) 92222-2222");
        when(personRepository.existsByPhone(personCreateRequestDTO.getPhone())).thenReturn(true);

        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfPhoneIsNotUnique(personCreateRequestDTO, null)
        );
    }

    @Test
    void updatePersonIfPhoneIsUnique() {
        var person = new Person();
        person.setId(1L);
        var personUpdateRequestDTO = new PersonUpdateRequestDTO();
        personUpdateRequestDTO.setPhone("(11) 91111-1111");
        when(personRepository.existsByPhoneAndIdNot(personUpdateRequestDTO.getPhone(), person.getId())).thenReturn(false);

        assertDoesNotThrow(() -> personService.throwIfPhoneIsNotUnique(personUpdateRequestDTO, person.getId()));
    }

    @Test
    void dontUpdatePersonIfPhoneIsNotUnique() {
        var person = new Person();
        person.setId(1L);
        var personUpdateRequestDTO = new PersonUpdateRequestDTO();
        personUpdateRequestDTO.setPhone("(11) 92222-2222");
        when(personRepository.existsByPhoneAndIdNot(personUpdateRequestDTO.getPhone(), person.getId())).thenReturn(true);
        
        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfPhoneIsNotUnique(personUpdateRequestDTO, person.getId())
        );
    }
}
