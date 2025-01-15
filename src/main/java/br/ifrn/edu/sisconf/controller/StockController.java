package br.ifrn.edu.sisconf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifrn.edu.sisconf.domain.dtos.StockResponseDTO;
import br.ifrn.edu.sisconf.service.StockService;

@RestController
@RequestMapping("/api/stocks/entrepreneurs/{entrepreneurId}")
public class StockController {
    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<StockResponseDTO> get(@PathVariable Long entrepreneurId) {
        return ResponseEntity.ok(stockService.getByEntrepreneurId(entrepreneurId));
    }
}
