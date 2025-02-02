package br.ifrn.edu.sisconf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifrn.edu.sisconf.domain.dtos.CountryStateResponseDTO;
import br.ifrn.edu.sisconf.service.CountryStateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/country-states")
@Tag(name = "Country State")
public class CountryStateController {
    @Autowired
    private CountryStateService countryStateService;

    @GetMapping
    @Operation(description = "Lista todos os estados do Brasil")
    public ResponseEntity<List<CountryStateResponseDTO>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
            countryStateService.getAll()
        );
    }
}
