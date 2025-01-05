package br.ifrn.edu.sisconf.domain.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdersGroupRequestDTO {
    @NotBlank(message = "A quantidade do item não pode ser vazia")
    @Valid
    private Integer itemQuantity;
}
