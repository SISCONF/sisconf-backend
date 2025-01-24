package br.ifrn.edu.sisconf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifrn.edu.sisconf.domain.dtos.CityResponseDTO;
import br.ifrn.edu.sisconf.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cities")
@Tag(name = "City")
public class CityController {
    @Autowired
    private CityService cityService;

    @Operation(description = "Obter dados da cidade")
    @GetMapping("/{id}")
    public ResponseEntity<CityResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cityService.getById(id));
    }
}
