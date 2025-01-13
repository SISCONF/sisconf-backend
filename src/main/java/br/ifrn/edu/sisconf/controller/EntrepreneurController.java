package br.ifrn.edu.sisconf.controller;

import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.CreateEntrepreneurGroup;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.CreatePersonGroup;
import br.ifrn.edu.sisconf.domain.dtos.Person.UpdatePersonGroup;
import br.ifrn.edu.sisconf.service.EntrepreneurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.groups.Default;

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
    public ResponseEntity<EntrepreneurResponseDTO> save(
        @Validated({CreatePersonGroup.class, CreateEntrepreneurGroup.class, Default.class}) 
        @RequestBody EntrepreneurRequestDTO entrepreneurRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entrepreneurService.save(entrepreneurRequestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntrepreneurResponseDTO> update(
        @PathVariable Long id,
        @Validated({UpdatePersonGroup.class, CreateEntrepreneurGroup.class, Default.class}) 
        @RequestBody EntrepreneurRequestDTO entrepreneurRequestDTO
    ) {
        return ResponseEntity.ok(entrepreneurService.update(id, entrepreneurRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        entrepreneurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
