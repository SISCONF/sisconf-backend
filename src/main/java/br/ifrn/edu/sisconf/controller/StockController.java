package br.ifrn.edu.sisconf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifrn.edu.sisconf.constants.KeycloakConstants;
import br.ifrn.edu.sisconf.domain.dtos.StockFoodDeleteRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.StockFoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.StockResponseDTO;
import br.ifrn.edu.sisconf.security.SisconfUserDetails;
import br.ifrn.edu.sisconf.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/stocks/entrepreneurs/{entrepreneurId}")
@PreAuthorize(KeycloakConstants.ROLE_MANAGE_STOCK)
@Tag(name = "Stocks")
public class StockController {
    @Autowired
    private StockService stockService;

    @GetMapping
    @Operation(description = "Obtém estoque do empreendedor")
    public ResponseEntity<StockResponseDTO> get(
        @PathVariable Long entrepreneurId, 
        @AuthenticationPrincipal SisconfUserDetails userDetails
    ) {
        return ResponseEntity.ok(stockService.getByEntrepreneurId(entrepreneurId, userDetails));
    }

    @PostMapping
    @Operation(description = "Associa comidas ao estoque do empreendedor logado")
    public ResponseEntity<StockResponseDTO> associateFoods(
        @PathVariable Long entrepreneurId,
        @RequestBody @Valid StockFoodRequestDTO stockFoodRequestDTO, 
        @AuthenticationPrincipal SisconfUserDetails userDetails
    ) {
        return ResponseEntity.ok(stockService.associateFoods(entrepreneurId, stockFoodRequestDTO, userDetails));
    }

    @PatchMapping
    @Operation(description = "Atualiza a quantidade de uma comida no estoque do empreendedor logado")
    public ResponseEntity<Void> updateStockFoodQuantity(
        @PathVariable Long entrepreneurId,
        @RequestBody @Valid StockFoodRequestDTO stockFoodRequestDTO,
        @AuthenticationPrincipal SisconfUserDetails userDetails
    ) {
        stockService.updateStockFoodQuantity(entrepreneurId, stockFoodRequestDTO, userDetails);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/foods")
    @Operation(description = "Remove uma comida do estoque do empreendedor logado")
    public ResponseEntity<Void> removeFoodFromStock(
        @PathVariable Long entrepreneurId, 
        @RequestBody @Valid StockFoodDeleteRequestDTO stockFoodDeleteRequestDTO,
        @AuthenticationPrincipal SisconfUserDetails userDetails
    ) {
        stockService.removeFoodsFromStock(entrepreneurId, stockFoodDeleteRequestDTO, userDetails);
        return ResponseEntity.noContent().build();
    }
}
