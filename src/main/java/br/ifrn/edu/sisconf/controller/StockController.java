package br.ifrn.edu.sisconf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifrn.edu.sisconf.domain.dtos.StockResponseDTO;
import br.ifrn.edu.sisconf.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/stocks")
@Tag(name = "Stocks")
public class StockController {
    @Autowired
    private StockService stockService;

    @GetMapping("/entrepreneurs/{entrepreneurId}")
    @Operation(description = "Obter estoque do empreendedor") 
    public ResponseEntity<StockResponseDTO> get(@PathVariable Long entrepreneurId) {
        return ResponseEntity.ok(stockService.getByEntrepreneurId(entrepreneurId));
    }
}
