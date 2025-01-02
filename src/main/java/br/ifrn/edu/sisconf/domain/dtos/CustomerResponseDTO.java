package br.ifrn.edu.sisconf.domain.dtos;

import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.enums.CustomerCategory;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private PersonResponseDTO person;
    private CustomerCategory category;

    public CustomerResponseDTO(Customer customer) {
        id = customer.getId();
        person = new PersonResponseDTO(
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
        category = customer.getCategory();
    }
}
