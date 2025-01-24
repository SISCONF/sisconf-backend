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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifrn.edu.sisconf.domain.dtos.Order.OrderRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Order.OrderResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Order.OrderUpdateRequestDTO;
import br.ifrn.edu.sisconf.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @Operation(description = "Adicionar um novo pedido")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestHeader("X-Customer-ID") Long customerId, @Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(customerId, orderRequestDTO));
    }

    @GetMapping
    @Operation(description = "Listar todos os pedidos")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    @Operation(description = "Obter dados de um pedido")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    @Operation(description = "Atualizar um pedido")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderUpdateRequestDTO orderUpdateRequestDTO) {
        return ResponseEntity.ok(orderService.updateOrder(id, orderUpdateRequestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Apagar um pedido")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }


}
