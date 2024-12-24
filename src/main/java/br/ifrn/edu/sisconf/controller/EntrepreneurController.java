package br.ifrn.edu.sisconf.controller;

import br.ifrn.edu.sisconf.domain.dtos.EntrepreneurResponseDTO;
import br.ifrn.edu.sisconf.service.EntrepreneurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
