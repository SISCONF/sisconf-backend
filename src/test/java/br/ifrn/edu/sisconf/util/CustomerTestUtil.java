package br.ifrn.edu.sisconf.util;

import br.ifrn.edu.sisconf.domain.Address;
import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.dtos.AddressResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.PersonResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerResponseDTO;
import br.ifrn.edu.sisconf.domain.enums.CustomerCategory;

public class CustomerTestUtil {
    public static Customer createValidCustomer() {
        return new Customer(
            CustomerCategory.MARKETER,
            PersonTestUtil.createValidPerson(null),
            null
        );
    }

    public static CustomerRequestDTO toValidRequestDTO(Customer customer) {
        return new CustomerRequestDTO(
            customer.getCategory(),
            PersonTestUtil.toValidRequestDTO(customer.getPerson())
        );
    } 

    public static CustomerRequestDTO createValidCustomerCreateRequestDTO() {
        return new CustomerRequestDTO(
            CustomerCategory.MARKETER,
            PersonTestUtil.createValidPersonCreateRequestDTO(null)
        );
    }

    public static Customer createCustomerFromDto(CustomerRequestDTO customerCreateRequestDTO) {
        var customer = new Customer(
            customerCreateRequestDTO.getCategory(),
            new Person(
                null,
                customerCreateRequestDTO.getPerson().getFirstName(),
                customerCreateRequestDTO.getPerson().getLastName(),
                customerCreateRequestDTO.getPerson().getEmail(),
                customerCreateRequestDTO.getPerson().getCpf(),
                customerCreateRequestDTO.getPerson().getCnpj(),
                customerCreateRequestDTO.getPerson().getPhone(),
                new Address(
                    customerCreateRequestDTO.getPerson().getAddress().getStreet(),
                    customerCreateRequestDTO.getPerson().getAddress().getZipCode(),
                    customerCreateRequestDTO.getPerson().getAddress().getNeighbourhood(),
                    customerCreateRequestDTO.getPerson().getAddress().getNumber(),
                    new City()
                ),
                null,
                null
            ),
            null
        );
        customer.getPerson().setCustomer(customer);
        customer.getPerson().getAddress().getCity().setId(
            customerCreateRequestDTO.getPerson().getAddress().getCity()
        );
        return customer;
    }

    public static CustomerResponseDTO getResponseDTO(Customer customer) {
        Long id = customer.getId();
        var personResponseDTO = new PersonResponseDTO(
            id,
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
        );
        var category = customer.getCategory();
        return new CustomerResponseDTO(id, personResponseDTO, category);
    }
}
