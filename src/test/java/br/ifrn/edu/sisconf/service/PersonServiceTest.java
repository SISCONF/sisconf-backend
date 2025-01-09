package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
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
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCpf("123.456.789-10");
        when(personRepository.existsByCpf(personRequestDTO.getCpf())).thenReturn(false);
        
        assertDoesNotThrow(() -> 
            personService.throwIfCpfIsNotUnique(
                personRequestDTO.getCpf(), 
                null
            )
        );
    }

    @Test
    void createPersonWhileCPFUsed() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCpf("987.654.321-00");

        when(personRepository.existsByCpf(personRequestDTO.getCpf())).thenReturn(true);

        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfCpfIsNotUnique(personRequestDTO.getCpf(), null)
        );
    }

    @Test
    void updatePersonWhileCPFUnused() {
        var person = new Person();
        person.setId(1L);
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCpf("123.456.789-10");
        when(personRepository.existsByCpfAndIdNot(personRequestDTO.getCpf(), person.getId()))
            .thenReturn(false);

        assertDoesNotThrow(() -> 
            personService.throwIfCpfIsNotUnique(
                personRequestDTO.getCpf(), 
                person.getId()
            )
        );
    }

    @Test
    void updatePersonWhileCPFUsed() {
        var person = new Person();
        person.setId(1L);
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCpf("987.654.321-00");
        when(personRepository.existsByCpfAndIdNot(personRequestDTO.getCpf(), person.getId()))
            .thenReturn(true);

        assertThrowsExactly(
            BusinessException.class, 
            () -> personService.throwIfCpfIsNotUnique(personRequestDTO.getCpf(), person.getId())
        );
    }

    @Test
    void createPersonIfPasswordsMatch() {
        var personRequestDTO = new PersonRequestDTO();
        final String password = "abc12345";
        personRequestDTO.setPassword(password);
        personRequestDTO.setPassword2(password);

        assertDoesNotThrow(() -> personService.throwIfPasswordsDontMatch(
            personRequestDTO.getPassword(),
            personRequestDTO.getPassword2()
        ));
    }

    @Test
    void dontCreatePersonIfPasswordsMatch() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPassword("abc123456");
        personRequestDTO.setPassword2("abc12345");

        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfPasswordsDontMatch(
                personRequestDTO.getPassword(),
                personRequestDTO.getPassword2()
            )
        );
    }

    @Test
    void dontCreatePersonIfEmailIsNotUnique() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setEmail("teste2@email.com");
        when(personRepository.existsByEmail(personRequestDTO.getEmail())).thenReturn(true);

        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfEmailIsNotUnique(personRequestDTO.getEmail())
        );
    }

    @Test
    void createPersonIfEmailIsUnique() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setEmail("teste@email.com");
        when(personRepository.existsByEmail(personRequestDTO.getEmail())).thenReturn(false);

        assertDoesNotThrow(() -> personService.throwIfEmailIsNotUnique(personRequestDTO.getEmail()));
    }

    @Test
    void dontCreatePersonIfCnpjIsNotUnique() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCnpj("22.222.222/0001-22");
        when(personRepository.existsByCnpj(personRequestDTO.getCnpj())).thenReturn(true);

        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfCnpjIsNotUnique(personRequestDTO.getCnpj(), null)
        );
    }

    @Test
    void createPersonIfCnpjIsUnique() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCnpj("11.111.111/0001-11");
        when(personRepository.existsByCnpj(personRequestDTO.getCnpj())).thenReturn(false);

        assertDoesNotThrow(() -> 
            personService.throwIfCnpjIsNotUnique(
                personRequestDTO.getCnpj(), 
                null
            )
        );
    }

    @Test
    void dontUpdatePersonIfCnpjIsNotUnique() {
        var person = new Person();
        person.setId(1L);
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCnpj("22.222.222/0001-22");
        when(personRepository.existsByCnpjAndIdNot(personRequestDTO.getCnpj(), person.getId()))
            .thenReturn(true);

        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfCnpjIsNotUnique(personRequestDTO.getCnpj(), person.getId())
        );
    }

    @Test
    void updatePersonIfCnpjIsUnique() {
        var person = new Person();
        person.setId(1L);
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setCnpj("11.111.111/0001-11");
        when(personRepository.existsByCnpjAndIdNot(personRequestDTO.getCnpj(), person.getId()))
            .thenReturn(false);

        assertDoesNotThrow(() -> personService.throwIfCnpjIsNotUnique(personRequestDTO.getCnpj(), person.getId()));
    }

    @Test
    void createPersonIfPhoneIsUnique() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPhone("(11) 91111-1111");
        when(personRepository.existsByPhone(personRequestDTO.getPhone())).thenReturn(false);

        assertDoesNotThrow(() -> personService.throwIfPhoneIsNotUnique(
            personRequestDTO.getPhone(),
            null
        ));
    }

    @Test
    void dontCreatePersonIfPhoneIsNotUnique() {
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPhone("(11) 92222-2222");
        when(personRepository.existsByPhone(personRequestDTO.getPhone())).thenReturn(true);

        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfPhoneIsNotUnique(personRequestDTO.getPhone(), null)
        );
    }

    @Test
    void updatePersonIfPhoneIsUnique() {
        var person = new Person();
        person.setId(1L);
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPhone("(11) 91111-1111");
        when(personRepository.existsByPhoneAndIdNot(personRequestDTO.getPhone(), person.getId()))
            .thenReturn(false);

        assertDoesNotThrow(() -> 
            personService.throwIfPhoneIsNotUnique(
                personRequestDTO.getPhone(), 
                person.getId()
            )
        );
    }

    @Test
    void dontUpdatePersonIfPhoneIsNotUnique() {
        var person = new Person();
        person.setId(1L);
        var personRequestDTO = new PersonRequestDTO();
        personRequestDTO.setPhone("(11) 92222-2222");
        when(personRepository.existsByPhoneAndIdNot(personRequestDTO.getPhone(), person.getId()))
            .thenReturn(true);
        
        assertThrowsExactly(
            BusinessException.class,
            () -> personService.throwIfPhoneIsNotUnique(personRequestDTO.getPhone(), person.getId())
        );
    }
}
