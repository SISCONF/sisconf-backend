package br.ifrn.edu.sisconf.service;

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
        return customerMapper.toListResponse(customers);
    }

    public CustomerResponseDTO getById(Long id) {
        Customer customer = getCustomerById(id);
        return customerMapper.toResponse(customer);
    }

    public CustomerResponseDTO save(CustomerCreateRequestDTO customerCreateRequestDTO) {
        PersonCreateRequestDTO personCreateRequestDTO = customerCreateRequestDTO.getPerson();
        personService.throwIfPasswordsDontMatch(personCreateRequestDTO);
        personService.throwIfCpfIsNotUnique(personCreateRequestDTO);
        personService.throwIfEmailIsNotUnique(personCreateRequestDTO);
        personService.throwIfPhoneIsNotUnique(personCreateRequestDTO);

        cityService.getById(personCreateRequestDTO.getAddress().getCity());

        var customer = customerMapper.toEntity(customerCreateRequestDTO);
        UserRegistrationRecord userRegistrationRecord = new UserRegistrationRecord(
                personCreateRequestDTO.getFirstName(),
                personCreateRequestDTO.getLastName(),
                personCreateRequestDTO.getPassword(),
                personCreateRequestDTO.getEmail()
        );
        UserRegistrationResponse userRegistrationResponse = keycloakUserService.createUser(userRegistrationRecord);
        customer.getPerson().setKeycloakId(userRegistrationResponse.keycloakId());
        customerRepository.save(customer);
        return customerMapper.toResponse(customer);
    }

    public CustomerResponseDTO update(CustomerUpdateRequestDTO customerUpdateRequestDTO, Long id) {
        Customer customer = getCustomerById(id);
        cityService.getById(customerUpdateRequestDTO.getPerson().getAddress().getCity());

        UserUpdateRecord userUpdateRecord = new UserUpdateRecord(
                customer.getPerson().getKeycloakId(),
                customerUpdateRequestDTO.getPerson().getFirstName(),
                customerUpdateRequestDTO.getPerson().getLastName()
        );
        keycloakUserService.updateUser(userUpdateRecord);

        customerMapper.updateEntityFromDTO(customerUpdateRequestDTO, customer);
        return customerMapper.toResponse(customer);
    }
}
