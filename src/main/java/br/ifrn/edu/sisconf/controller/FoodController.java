package br.ifrn.edu.sisconf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifrn.edu.sisconf.domain.dtos.FoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.FoodResponseDTO;
import br.ifrn.edu.sisconf.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/foods")
@Tag(name = "Foods")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @PostMapping
    
    @Operation(description = "Adicionar novo alimento")
    public ResponseEntity<FoodResponseDTO> createFood(@RequestBody @Valid FoodRequestDTO createFoodDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(foodService.createFood(createFoodDto));
    }

    @GetMapping
    @Operation(description = "Listar todos alimentos")
    public ResponseEntity<List<FoodResponseDTO>> getAllFoods() {
        return ResponseEntity.ok(foodService.listAllFoods());
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Apagar alimento")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        foodService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(description = "Obter dados do alimento")
    public ResponseEntity<FoodResponseDTO> getFood(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.getFood(id));
    }

    @PutMapping("/{id}")
    @Operation(description = "Atualizar alimento")
    public ResponseEntity<FoodResponseDTO> update(@PathVariable Long id, @RequestBody @Valid FoodRequestDTO foodDto) {
        return ResponseEntity.ok(foodService.update(id, foodDto));
    }
}
