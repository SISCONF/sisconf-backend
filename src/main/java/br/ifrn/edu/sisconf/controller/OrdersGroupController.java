package br.ifrn.edu.sisconf.controller;

import br.ifrn.edu.sisconf.constants.KeycloakConstants;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupResponseDTO;
import br.ifrn.edu.sisconf.service.OrdersGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders-group")
@Tag(name = "Orders Group")
@PreAuthorize(value = KeycloakConstants.ROLE_MANAGE_ORDERS_GROUP)
public class OrdersGroupController {
    @Autowired
    private OrdersGroupService ordersGroupService;

    @PostMapping
    @Operation(description = "Criar um novo grupo de pedidos")
    public ResponseEntity<OrdersGroupResponseDTO> save(@RequestBody @Valid OrdersGroupRequestDTO ordersGroupRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordersGroupService.save(ordersGroupRequestDTO));
    }

    @GetMapping
    @Operation(description = "Listar grupos de pedidos")
    public ResponseEntity<List<OrdersGroupResponseDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(ordersGroupService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(description = "Buscar grupo de pedido por id")
    public ResponseEntity<OrdersGroupResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(ordersGroupService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(description = "Atualizar grupo de pedido")
    public ResponseEntity<OrdersGroupResponseDTO> update(@PathVariable Long id, @RequestBody @Valid OrdersGroupRequestDTO ordersGroupRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(ordersGroupService.update(id, ordersGroupRequestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Excluir grupo de pedido")
    public ResponseEntity<OrdersGroupResponseDTO> delete(@PathVariable Long id) {
        ordersGroupService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
