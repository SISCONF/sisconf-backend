package br.ifrn.edu.sisconf.controller;

import br.ifrn.edu.sisconf.domain.dtos.CustomerCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.CustomerResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.CustomerUpdateRequestDTO;
import br.ifrn.edu.sisconf.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> save(@Valid @RequestBody CustomerCreateRequestDTO customerCreateRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.save(customerCreateRequestDTO));
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
    public ResponseEntity<CustomerResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CustomerUpdateRequestDTO customerUpdateRequestDTO) {
        return ResponseEntity.ok(customerService.update(customerUpdateRequestDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
