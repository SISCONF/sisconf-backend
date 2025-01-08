package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.dtos.CustomerResponseDTO;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.CustomerMapper;
import br.ifrn.edu.sisconf.repository.CustomerRepository;
import br.ifrn.edu.sisconf.service.keycloak.KeycloakUserService;

public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private PersonService personService;

    @Mock
    private KeycloakUserService keycloakUserService;

    @InjectMocks
    CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldThrowWhenCustomerIdDoesNotExist() {
        final Long id = 2L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrowsExactly(
            ResourceNotFoundException.class,
            () -> customerService.getById(id)
        );
    }

    @Test
    void shouldReturnCustomerWhenCustomerIdExist() {
        var customer = Instancio.create(Customer.class);
        var customerResponseDTO = new CustomerResponseDTO(customer);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerMapper.toResponseDTO(customer)).thenReturn(customerResponseDTO);

        var returnedCustomer = customerService.getById(customer.getId());
        assertNotNull(returnedCustomer);
        assertEquals(returnedCustomer, customerResponseDTO);
    }

    @Test
    void shouldReturnCustomersList() {
        var customer = Instancio.create(Customer.class); // Doesn't matter which data is contained in it
        when(customerRepository.findAll()).thenReturn(List.of(customer));
        when(customerMapper.toDTOList(List.of(customer))).thenReturn(
            List.of(new CustomerResponseDTO(customer))
        );

        List<CustomerResponseDTO> customerResponseDTOs = customerService.getAll();
        assertEquals(1, customerResponseDTOs.size());
    }

    @Test
    void shouldRemoveWhenClientWithIdExists() {
        var customer = Instancio.create(Customer.class);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        customerService.deleteById(customer.getId());
        verify(customerRepository, times(1)).deleteById(customer.getId());
        verify(keycloakUserService, times(1)).deleteById(
            customer.getPerson().getKeycloakId()
        );
    }

    @Test
    void shouldNotRemoveWhenClientWithIdDoesNotExist() {
        final Long unexistingId = -1L;
        when(customerRepository.findById(unexistingId)).thenThrow(
            ResourceNotFoundException.class
        );

        assertThrowsExactly(
            ResourceNotFoundException.class, 
            () -> customerService.deleteById(unexistingId)
        );
        verify(customerRepository, never()).deleteById(unexistingId);
        verify(keycloakUserService, never()).deleteById(null);
    }
}
