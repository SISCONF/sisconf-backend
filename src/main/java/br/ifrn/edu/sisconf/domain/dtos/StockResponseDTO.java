package br.ifrn.edu.sisconf.domain.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockResponseDTO {
    private Long id;
    private Long entrepreneurId;
    private List<FoodResponseDTO> foods;
}
