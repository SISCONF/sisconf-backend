package br.ifrn.edu.sisconf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.CreatePersonGroup;
import br.ifrn.edu.sisconf.domain.dtos.Person.UpdatePersonGroup;
import br.ifrn.edu.sisconf.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;

@RestController
@RequestMapping("/api/customers")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    @PreAuthorize("permitAll()")
    @Operation(description = "Adicionar novo cliente")
    public ResponseEntity<CustomerResponseDTO> save(
        @Validated({CreatePersonGroup.class, Default.class}) 
        @RequestBody CustomerRequestDTO customerCreateRequestDTO
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(customerService.save(customerCreateRequestDTO));
    }

    @GetMapping("/me")
    @Operation(description = "Informações sobre o cliente autenticado")
    public ResponseEntity<CustomerResponseDTO> me(
        JwtAuthenticationToken jwtToken
    ) {
        String keycloakId = jwtToken.getToken().getClaimAsString("sub");
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getByKeycloakId(keycloakId));
    }

    @GetMapping("/{id}")
    @Operation(description = "Obter dados do cliente")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(customerService.getById(id));
    }

    @GetMapping
    @Operation(description = "Listar todos clientes")
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @PutMapping("/{id}")
    @Operation(description = "Atualizar dados do cliente")
    public ResponseEntity<CustomerResponseDTO> update(
        @PathVariable Long id, 
        @Validated({UpdatePersonGroup.class, Default.class}) 
        @RequestBody CustomerRequestDTO customerRequestDTO
    ) {
        return ResponseEntity.ok(customerService.update(customerRequestDTO, id));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Apagar cliente")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
