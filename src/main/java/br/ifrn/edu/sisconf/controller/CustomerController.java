package br.ifrn.edu.sisconf.controller;

import br.ifrn.edu.sisconf.domain.dtos.CustomerCreateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.CustomerResponseDTO;
import br.ifrn.edu.sisconf.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> save(@Valid @RequestBody CustomerCreateRequestDTO customerCreateRequestDTO) {
        return ResponseEntity.ok(customerService.save(customerCreateRequestDTO));
    }
}
