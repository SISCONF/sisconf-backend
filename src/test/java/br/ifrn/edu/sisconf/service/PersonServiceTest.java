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
}
