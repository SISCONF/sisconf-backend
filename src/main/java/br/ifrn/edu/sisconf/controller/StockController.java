package br.ifrn.edu.sisconf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifrn.edu.sisconf.domain.dtos.StockFoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.StockResponseDTO;
import br.ifrn.edu.sisconf.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/stocks/entrepreneurs/{entrepreneurId}")
@Tag(name = "Stocks")
public class StockController {
    @Autowired
    private StockService stockService;

    @GetMapping
    @Operation(description = "Obtém estoque do empreendedor") 
    public ResponseEntity<StockResponseDTO> get(@PathVariable Long entrepreneurId) {
        return ResponseEntity.ok(stockService.getByEntrepreneurId(entrepreneurId));
    }

    @PostMapping
    @Operation(description = "Associa comidas ao estoque do empreendedor logado")
    public ResponseEntity<StockResponseDTO> associateFoods(@PathVariable Long entrepreneurId, @RequestBody @Valid StockFoodRequestDTO stockFoodRequestDTO) {
        return ResponseEntity.ok(stockService.associateFoods(entrepreneurId, stockFoodRequestDTO));
    }

    @PatchMapping
    @Operation(description = "Atualiza a quantidade de uma comida no estoque do empreendedor logado")
    public ResponseEntity<Void> updateStockFoodQuantity(@PathVariable Long entrepreneurId, @RequestBody @Valid StockFoodRequestDTO stockFoodRequestDTO) {
        stockService.updateStockFoodQuantity(entrepreneurId, stockFoodRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/foods/{foodId}")
    @Operation(description = "Remove uma comida do estoque do empreendedor logado")
    public ResponseEntity<Void> removeFoodFromStock(@PathVariable Long entrepreneurId, @PathVariable Long foodId) {
        stockService.removeFoodFromStock(entrepreneurId, foodId);
        return ResponseEntity.noContent().build();
    }
}
