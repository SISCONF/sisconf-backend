package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.dtos.CustomerCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.CustomerResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.PersonCreateRequestDTO;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationRecord;
import br.ifrn.edu.sisconf.dto.keycloak.UserRegistrationResponse;
import br.ifrn.edu.sisconf.mapper.CustomerMapper;
import br.ifrn.edu.sisconf.repository.CustomerRepository;
import br.ifrn.edu.sisconf.service.keycloak.KeycloakUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
