package br.ifrn.edu.sisconf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifrn.edu.sisconf.domain.dtos.Person.LoginRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Person.LoginResponseDTO;
import br.ifrn.edu.sisconf.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/people")
@Tag(name = "Person")
public class PersonController {
    @Autowired
    private PersonService personService;

    @PostMapping("/login")
    @Operation(description = "Realiza o login do usuário")
    public ResponseEntity<LoginResponseDTO> login(
        @Valid @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            personService.login(loginRequestDTO)
        );
    }
}
