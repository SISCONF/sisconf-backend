package br.ifrn.edu.sisconf.controller;

import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Customer.CustomerResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.CreatePersonGroup;
import br.ifrn.edu.sisconf.domain.dtos.Person.UpdatePersonGroup;
import br.ifrn.edu.sisconf.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.groups.Default;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@PreAuthorize("isAuthenticated()")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomerResponseDTO> save(
        @Validated({CreatePersonGroup.class, Default.class}) 
        @RequestBody CustomerRequestDTO customerCreateRequestDTO
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(customerService.save(customerCreateRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(customerService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {
        return ResponseEntity.ok(customerService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> update(
        @PathVariable Long id, 
        @Validated({UpdatePersonGroup.class, Default.class}) 
        @RequestBody CustomerRequestDTO customerRequestDTO
    ) {
        return ResponseEntity.ok(customerService.update(customerRequestDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
