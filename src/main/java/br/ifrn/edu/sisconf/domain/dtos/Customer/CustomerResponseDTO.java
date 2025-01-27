package br.ifrn.edu.sisconf.domain.dtos.Customer;

import br.ifrn.edu.sisconf.domain.dtos.PersonResponseDTO;
import br.ifrn.edu.sisconf.domain.enums.CustomerCategory;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private PersonResponseDTO person;
    private CustomerCategory category;
}
