package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import br.ifrn.edu.sisconf.domain.Address;
import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
import br.ifrn.edu.sisconf.domain.enums.CustomerCategory;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.CustomerMapper;
import br.ifrn.edu.sisconf.repository.CustomerRepository;
import br.ifrn.edu.sisconf.service.keycloak.KeycloakUserService;
import br.ifrn.edu.sisconf.util.CustomerTestUtil;
import br.ifrn.edu.sisconf.util.PersonTestUtil;

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

    @Test
    public void shouldUpdateCustomerWhenIdAndDataValid() {
        var customer = CustomerTestUtil.createValidCustomer();
        customer.setId(1L);
        var updateCustomerRequestDTO = CustomerTestUtil.toValidRequestDTO(customer);

        updateCustomerRequestDTO.getPerson().setEmail(null);
        updateCustomerRequestDTO.getPerson().setPassword(null);
        updateCustomerRequestDTO.getPerson().setPassword2(null);
        updateCustomerRequestDTO.setCategory(CustomerCategory.OTHER);
        updateCustomerRequestDTO.getPerson().setFirstName("Novo First Name");
        updateCustomerRequestDTO.getPerson().setLastName("Novo Last Name");
        updateCustomerRequestDTO.getPerson().setCpf("987.654.321-00");
        updateCustomerRequestDTO.getPerson().setCnpj("98.765.432/1000-00");
        updateCustomerRequestDTO.getPerson().setPhone("(22) 92222-2222");
        updateCustomerRequestDTO.getPerson().getAddress().setStreet("Nova Rua");
        updateCustomerRequestDTO.getPerson().getAddress().setNeighbourhood("Nova Vizinhança");
        updateCustomerRequestDTO.getPerson().getAddress().setNumber(100);
        updateCustomerRequestDTO.getPerson().getAddress().setZipCode("22222-222");
        updateCustomerRequestDTO.getPerson().getAddress().setCity(20L);

        var userUpdateRecord = PersonTestUtil.createUserUpdateRecord(
            customer.getPerson().getKeycloakId(), 
            updateCustomerRequestDTO.getPerson()
        );
        var updatedCustomer = new Customer(
            updateCustomerRequestDTO.getCategory(),
            new Person(
                customer.getPerson().getKeycloakId(),
                updateCustomerRequestDTO.getPerson().getFirstName(),
                updateCustomerRequestDTO.getPerson().getLastName(),
                customer.getPerson().getEmail(),
                updateCustomerRequestDTO.getPerson().getCpf(),
                updateCustomerRequestDTO.getPerson().getCnpj(),
                updateCustomerRequestDTO.getPerson().getPhone(),
                new Address(
                    updateCustomerRequestDTO.getPerson().getAddress().getStreet(),
                    updateCustomerRequestDTO.getPerson().getAddress().getZipCode(),
                    updateCustomerRequestDTO.getPerson().getAddress().getNeighbourhood(),
                    updateCustomerRequestDTO.getPerson().getAddress().getNumber(),
                    new City()
                )
            ),
            null
        );
        updatedCustomer.getPerson().getAddress().getCity().setId(
            updateCustomerRequestDTO.getPerson().getAddress().getCity()
        );
        var expectedResponseDTO = CustomerTestUtil.getResponseDTO(updatedCustomer);

        when(customerRepository.findById(customer.getId())).thenReturn(
            Optional.of(customer)
        );
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toResponseDTO(customer)).thenReturn(
            expectedResponseDTO
        );

        var actualResponseDTO = customerService.update(updateCustomerRequestDTO, customer.getId());
        
        verify(keycloakUserService, times(1)).update(
            userUpdateRecord
        );
        verify(customerMapper, times(1)).updateEntityFromDTO(
            updateCustomerRequestDTO, 
            customer
        );
        assertEquals(expectedResponseDTO, actualResponseDTO);
    }

    @Test
    public void shouldNotUpdateCustomerWhenIdInvalid() {
        when(customerRepository.findById(-1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> customerService.update(
                new CustomerRequestDTO(
                    CustomerCategory.ENTREPRENEUR,
                    new PersonRequestDTO()
                ),
                -1L
            )
        );

        assertEquals("Cliente com esse ID não existe", exception.getMessage());
    }
}
