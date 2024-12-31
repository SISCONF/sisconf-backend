package br.ifrn.edu.sisconf.domain.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    @NotNull(message = "O ID do cliente é obrigatório.")
    private Long customerId;

    @NotEmpty(message = "A lista de IDs de frutas e verduras não pode estar vazia.")
    private List<Long> foodIds;
}
