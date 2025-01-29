package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.constants.KeycloakConstants;
import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.PersonRequestDTO;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationResponse;
import br.ifrn.edu.sisconf.dto.keycloak.UserUpdateRecord;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.CustomerMapper;
import br.ifrn.edu.sisconf.repository.CustomerRepository;
import br.ifrn.edu.sisconf.service.keycloak.KeycloakUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private CustomerMapper customerMapper;

    public CustomerResponseDTO getByKeycloakId(String keycloakId) {
        var customer = customerRepository.findByPersonKeycloakId(keycloakId).orElseThrow(() ->
            new ResourceNotFoundException(
                "Usuário não encontrado"
            )
        );
        return customerMapper.toResponseDTO(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Cliente com esse ID não existe"
                )
            );
    }

    public List<CustomerResponseDTO> getAll() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toDTOList(customers);
    }

    public CustomerResponseDTO getById(Long id) {
        Customer customer = getCustomerById(id);
        return customerMapper.toResponseDTO(customer);
    }

    public CustomerResponseDTO save(CustomerRequestDTO customerRequestDTO) {
        PersonRequestDTO personRequestDTO = customerRequestDTO.getPerson();
        personService.validatePersonCreation(personRequestDTO);

        var customer = customerMapper.toEntity(customerRequestDTO);
        UserRegistrationRecord userRegistrationRecord = new UserRegistrationRecord(
                personRequestDTO.getFirstName(),
                personRequestDTO.getLastName(),
                personRequestDTO.getPassword(),
                personRequestDTO.getEmail(),
                KeycloakConstants.CUSTOMER_GROUP_NAME
        );
        UserRegistrationResponse userRegistrationResponse = keycloakUserService
            .create(userRegistrationRecord);
        customer.getPerson().setKeycloakId(userRegistrationResponse.keycloakId());
        try {
            customerRepository.save(customer);
            return customerMapper.toResponseDTO(customer);
        } catch (Exception exception) {
            keycloakUserService.deleteById(userRegistrationResponse.keycloakId());
            throw exception;
        }
    }

    public CustomerResponseDTO update(
        CustomerRequestDTO customerRequestDTO,
        Long id,
        String loggedCustomerKeycloakId
    ) {
        var customer = getCustomerById(id);

        personService.throwIfLoggedPersonIsDifferentFromPersonResource(
            loggedCustomerKeycloakId,
            customer.getPerson()
        );

        personService.validatePersonUpdate(
            customerRequestDTO.getPerson(), 
            customer.getPerson().getId()
        );

        var userUpdateRecord = new UserUpdateRecord(
                customer.getPerson().getKeycloakId(),
                customerRequestDTO.getPerson().getFirstName(),
                customerRequestDTO.getPerson().getLastName()
        );
        keycloakUserService.update(userUpdateRecord);

        try {
            customerMapper.updateEntityFromDTO(customerRequestDTO, customer);
            var updatedCustomer = customerRepository.save(customer);
            return customerMapper.toResponseDTO(updatedCustomer);
        } catch (Exception exception) {
            // Must revert changes which were made to user on Keycloak
            var oldUserRecord = new UserUpdateRecord(
                    customer.getPerson().getKeycloakId(),
                    customer.getPerson().getFirstName(),
                    customer.getPerson().getLastName()
            );
            keycloakUserService.update(oldUserRecord);
            throw exception;
        }
    }

    public void deleteById(Long id, String loggedUserKeycloakId) {
        Customer customer = getCustomerById(id);

        personService.throwIfLoggedPersonIsDifferentFromPersonResource(
            loggedUserKeycloakId, 
            customer.getPerson()
        );

        customerRepository.deleteById(id);
        keycloakUserService.deleteById(customer.getPerson().getKeycloakId());
    }
}
