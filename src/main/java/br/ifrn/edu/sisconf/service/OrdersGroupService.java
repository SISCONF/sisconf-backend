package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.Order;
import br.ifrn.edu.sisconf.domain.OrdersGroup;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupResponseDTO;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.mapper.OrdersGroupMapper;
import br.ifrn.edu.sisconf.repository.OrderRepository;
import br.ifrn.edu.sisconf.repository.OrdersGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<Order> orders = orderRepository.findAllById(ordersGroupRequestDTO.getOrdersIds());

        if (orders.isEmpty()) {
            throw new BusinessException("É necessário ao menos um pedido para criar um grupo");
        }

        for (Order order : orders) {
            order.setOrdersGroup(ordersGroup);
        }
        ordersGroup.setOrders(orders);

        return ordersGroupMapper.toResponseDTO(ordersGroupRepository.save(ordersGroup));
    }
}
