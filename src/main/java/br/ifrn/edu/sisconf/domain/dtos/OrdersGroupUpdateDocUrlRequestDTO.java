package br.ifrn.edu.sisconf.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdersGroupUpdateDocUrlRequestDTO {
    @NotBlank(message = "Doc URL deve ser informada")
    private String docUrl;
}
