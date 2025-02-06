package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.Order;
import br.ifrn.edu.sisconf.domain.OrdersGroup;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupResponseDTO;
import br.ifrn.edu.sisconf.domain.enums.OrdersGroupStatus;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.OrdersGroupMapper;
import br.ifrn.edu.sisconf.repository.OrderRepository;
import br.ifrn.edu.sisconf.repository.OrdersGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersGroupService {
    @Autowired
    private OrdersGroupRepository ordersGroupRepository;

    @Autowired
    private OrdersGroupMapper ordersGroupMapper;
    @Autowired
    private OrderRepository orderRepository;

    public OrdersGroupResponseDTO save(OrdersGroupRequestDTO ordersGroupRequestDTO) {
        OrdersGroup ordersGroup = ordersGroupMapper.toEntity(ordersGroupRequestDTO);
        List<Order> orders = ordersGroupRequestDTO.getOrdersIds().stream()
                .map(orderId -> orderRepository.findById(orderId)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format("Pedido de id %d não encontrado", orderId))))
                .toList();

        if (orders.isEmpty()) {
            throw new BusinessException("É necessário ao menos um pedido para criar um grupo");
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Order order : orders) {
            order.setOrdersGroup(ordersGroup);
            total = total.add(order.getTotalPrice());
        }
        ordersGroup.setOrders(orders);
        ordersGroup.setTotalPrice(total);
        ordersGroup.setCurrentStatus(OrdersGroupStatus.DELIVERED);

        return ordersGroupMapper.toResponseDTO(ordersGroupRepository.save(ordersGroup));
    }
}
