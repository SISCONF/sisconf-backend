package br.ifrn.edu.sisconf.domain.dtos;

import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.enums.CustomerCategory;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PersonResponseDTO person;
    private CustomerCategory category;
}
