package br.ifrn.edu.sisconf.controller;

import br.ifrn.edu.sisconf.domain.dtos.EntrepreneurCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.EntrepreneurResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.EntrepreneurUpdateRequestDTO;
import br.ifrn.edu.sisconf.service.EntrepreneurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entrepreneur")
public class EntrepreneurController {
    @Autowired
    private EntrepreneurService entrepreneurService;

    @GetMapping("/{id}")
    public ResponseEntity<EntrepreneurResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(entrepreneurService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<EntrepreneurResponseDTO>> getAll() {
        return ResponseEntity.ok(entrepreneurService.getAll());
    }

    @PostMapping
    public ResponseEntity<EntrepreneurResponseDTO> save(@Valid @RequestBody EntrepreneurCreateRequestDTO entrepreneurCreateRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entrepreneurService.save(entrepreneurCreateRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntrepreneurResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EntrepreneurUpdateRequestDTO entrepreneurUpdateRequestDTO) {
        return ResponseEntity.ok(entrepreneurService.update(id, entrepreneurUpdateRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        entrepreneurService.deletebyId(id);
        return ResponseEntity.noContent().build();
    }
}
