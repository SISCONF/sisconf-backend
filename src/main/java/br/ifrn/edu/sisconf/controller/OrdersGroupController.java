package br.ifrn.edu.sisconf.controller;

import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupResponseDTO;
import br.ifrn.edu.sisconf.service.OrdersGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders-group")
@Tag(name = "Orders Group")
public class OrdersGroupController {
    @Autowired
    private OrdersGroupService ordersGroupService;

    @PostMapping
    @Operation(description = "Criar um novo grupo de pedidos")
    public ResponseEntity<OrdersGroupResponseDTO> save(OrdersGroupRequestDTO ordersGroupRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordersGroupService.save(ordersGroupRequestDTO));
    }
}
