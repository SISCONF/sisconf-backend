package br.ifrn.edu.sisconf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.CreateEntrepreneurGroup;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Entrepreneur.EntrepreneurResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.CreatePersonGroup;
import br.ifrn.edu.sisconf.domain.dtos.Person.UpdatePersonGroup;
import br.ifrn.edu.sisconf.security.SisconfUserDetails;
import br.ifrn.edu.sisconf.service.EntrepreneurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;

@RestController
@RequestMapping("/api/entrepreneurs")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Entrepreneur")
public class EntrepreneurController {
    @Autowired
    private EntrepreneurService entrepreneurService;

    @GetMapping("/me")
    public ResponseEntity<EntrepreneurResponseDTO> me(
        @AuthenticationPrincipal SisconfUserDetails userDetails
    ) {
        var entrepreneurResponseDTO = entrepreneurService.getByKeycloakId(
            userDetails.getKeycloakId()
        );
        return ResponseEntity.status(HttpStatus.OK).body(entrepreneurResponseDTO);
    }

    @PostMapping
    @Operation(description = "Adicionar novo empreendedor")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EntrepreneurResponseDTO> save(
        @Validated({CreatePersonGroup.class, CreateEntrepreneurGroup.class, Default.class}) 
        @RequestBody EntrepreneurRequestDTO entrepreneurRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(entrepreneurService.save(entrepreneurRequestDTO)
        );
    }

    @PutMapping("/{id}")
    @Operation(description = "Atualizar dados de um empreendedor")
    public ResponseEntity<EntrepreneurResponseDTO> update(
        @PathVariable Long id,
        @Validated({UpdatePersonGroup.class, CreateEntrepreneurGroup.class, Default.class}) 
        @RequestBody EntrepreneurRequestDTO entrepreneurRequestDTO
    ) {
        return ResponseEntity.ok(entrepreneurService.update(id, entrepreneurRequestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Apagar empreendedor")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        entrepreneurService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
