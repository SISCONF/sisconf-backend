package br.ifrn.edu.sisconf.controller;

import br.ifrn.edu.sisconf.domain.dtos.FoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.FoodResponseDTO;
import br.ifrn.edu.sisconf.service.FoodService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @PostMapping
    public ResponseEntity<FoodResponseDTO> createFood(@RequestBody FoodRequestDTO createFoodDto) {
        return ResponseEntity.ok(foodService.createFood(createFoodDto));
    }

    @GetMapping
    public ResponseEntity<List<FoodResponseDTO>> getAllFoods() {
        return ResponseEntity.ok(foodService.listAllFoods());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        foodService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponseDTO> getFood(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.getFood(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodResponseDTO> update(@PathVariable Long id, @RequestBody FoodRequestDTO foodDto) {
        return ResponseEntity.ok(foodService.update(id, foodDto));
    }
}
