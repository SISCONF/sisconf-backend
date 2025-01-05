package br.ifrn.edu.sisconf.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListOrdersGroupItemsResponseDTO {
    private Long id;
    private String name;
    private Integer totalQuantity;
}
