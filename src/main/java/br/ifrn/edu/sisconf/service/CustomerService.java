package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.constants.KeycloakConstants;
import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.dtos.CustomerCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.CustomerResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.CustomerUpdateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.PersonCreateRequestDTO;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationResponse;
import br.ifrn.edu.sisconf.dto.keycloak.UserUpdateRecord;
import br.ifrn.edu.sisconf.exception.BusinessException;
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
    private CityService cityService;

    @Autowired
    private PersonService personService;

    @Autowired
    private KeycloakUserService keycloakUserService;

    @Autowired
    private CustomerMapper customerMapper;

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new BusinessException("Usuário com esse ID não existe"));
    }

    public List<CustomerResponseDTO> getAll() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toDTOList(customers);
    }

    public CustomerResponseDTO getById(Long id) {
        Customer customer = getCustomerById(id);
        return customerMapper.toResponseDTO(customer);
    }

    public CustomerResponseDTO save(CustomerCreateRequestDTO customerCreateRequestDTO) {
        PersonCreateRequestDTO personCreateRequestDTO = customerCreateRequestDTO.getPerson();
        personService.validatePersonCreation(personCreateRequestDTO);

        var customer = customerMapper.toEntity(customerCreateRequestDTO);
        UserRegistrationRecord userRegistrationRecord = new UserRegistrationRecord(
                personCreateRequestDTO.getFirstName(),
                personCreateRequestDTO.getLastName(),
                personCreateRequestDTO.getPassword(),
                personCreateRequestDTO.getEmail(),
                KeycloakConstants.CLIENT_GROUP_NAME
        );
        UserRegistrationResponse userRegistrationResponse = keycloakUserService.create(userRegistrationRecord);
        customer.getPerson().setKeycloakId(userRegistrationResponse.keycloakId());
        try {
            customerRepository.save(customer);
            return customerMapper.toResponseDTO(customer);
        } catch (Exception exception) {
            keycloakUserService.deleteById(userRegistrationResponse.keycloakId());
            throw exception;
        }
    }

    public CustomerResponseDTO update(CustomerUpdateRequestDTO customerUpdateRequestDTO, Long id) {
        if (!customerRepository.existsById(id)) {
            throw new BusinessException("Usuário com esse ID não existe");
        }
        var customer = getCustomerById(id);
        personService.validatePersonUpdate(customerUpdateRequestDTO.getPerson(), customer.getPerson().getId());

        var userUpdateRecord = new UserUpdateRecord(
                customer.getPerson().getKeycloakId(),
                customerUpdateRequestDTO.getPerson().getFirstName(),
                customerUpdateRequestDTO.getPerson().getLastName()
        );
        keycloakUserService.update(userUpdateRecord);

        try {
            customerMapper.updateEntityFromDTO(customerUpdateRequestDTO, customer);
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

    public void deleteById(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.deleteById(id);
        keycloakUserService.deleteById(customer.getPerson().getKeycloakId());
    }
}
