package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ifrn.edu.sisconf.domain.Address;
import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.domain.CountryState;
import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.dtos.AddressResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.CustomerResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.PersonResponseDTO;
import br.ifrn.edu.sisconf.domain.enums.CustomerCategory;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.mapper.CustomerMapper;
import br.ifrn.edu.sisconf.repository.CustomerRepository;

public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowWhenCustomerIdDoesNotExist() {
        final Long id = 2L;
        when(customerRepository.findById(id)).thenThrow(BusinessException.class);

        assertThrowsExactly(
            BusinessException.class,
            () -> customerService.getById(id)
        );
    }

    @Test
    void shouldReturnCustomerWhenCustomerIdExist() {
        var customer = new Customer(
            CustomerCategory.MARKETER,
            new Person(
                "asd0a9a02", 
                "John", 
                "Doe", 
                "example@email.com", 
                "111.111.111-11", 
                null, 
                "(84) 91111-1111", 
                new Address(
                    "Rua", 
                    "10000-000", 
                    "Bairro", 
                    10, 
                    new City(
                        "Petrolina", 
                        new CountryState(
                            "Pernambuco", 
                            "PE"
                        )
                    )
                )
            ),
            new ArrayList<>()
        );
        customer.setId(1L);
        var customerResponseDTO = new CustomerResponseDTO(
            customer.getId(),
            new PersonResponseDTO(
                customer.getPerson().getId(), 
                customer.getPerson().getKeycloakId(),  
                customer.getPerson().getFirstName(), 
                customer.getPerson().getLastName(), 
                customer.getPerson().getEmail(), 
                customer.getPerson().getCpf(), 
                customer.getPerson().getCnpj(), 
                customer.getPerson().getPhone(), 
                new AddressResponseDTO(
                    customer.getPerson().getAddress().getId(), 
                    customer.getPerson().getAddress().getStreet(), 
                    customer.getPerson().getAddress().getZipCode(), 
                    customer.getPerson().getAddress().getNeighbourhood(), 
                    customer.getPerson().getAddress().getNumber(), 
                    customer.getPerson().getAddress().getCity().getId()
                    )
                ),
            customer.getCategory()
        );
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerMapper.toResponseDTO(customer)).thenReturn(customerResponseDTO);

        var returnedCustomer = customerService.getById(customer.getId());
        assertNotNull(returnedCustomer);
        assertEquals(returnedCustomer, customerResponseDTO);
    }
}
