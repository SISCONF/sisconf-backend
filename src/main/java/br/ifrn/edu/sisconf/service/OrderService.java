package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.Order;
import br.ifrn.edu.sisconf.domain.dtos.OrderRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderResponseDTO;
import br.ifrn.edu.sisconf.mapper.OrderMapper;
import br.ifrn.edu.sisconf.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper mapper;

    public OrderService(OrderRepository orderRepository, OrderMapper mapper) {
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    public OrderResponseDTO create(OrderRequestDTO dto) {
        Order order = mapper.toEntity(dto);
        order.setCode(UUID.randomUUID()); // Gera um código único para o pedido
        return mapper.toResponseDTO(orderRepository.save(order));
    }

    public List<OrderResponseDTO> listAll() {
        return orderRepository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        return mapper.toResponseDTO(order);
    }

    public OrderResponseDTO update(Long id, OrderRequestDTO dto) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        Order updatedOrder = mapper.toEntity(dto);
        updatedOrder.setId(existingOrder.getId());
        updatedOrder.setCode(existingOrder.getCode()); // Preserva o código
        return mapper.toResponseDTO(orderRepository.save(updatedOrder));
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
