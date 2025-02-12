package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.Order;
import br.ifrn.edu.sisconf.domain.OrderFood;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersGroupService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrdersGroupRepository ordersGroupRepository;

    @Autowired
    private OrdersGroupMapper ordersGroupMapper;

    public OrdersGroupResponseDTO save(OrdersGroupRequestDTO ordersGroupRequestDTO) {
        OrdersGroup ordersGroup = ordersGroupMapper.toEntity(ordersGroupRequestDTO);
        setOrdersToOrdersGroup(ordersGroupRequestDTO, ordersGroup);
        ordersGroup.setCurrentStatus(OrdersGroupStatus.PLACED);

        // futuramente virá do Serviço de processamento
        ordersGroup.setDocUrl("");

        return ordersGroupMapper.toResponseDTO(ordersGroupRepository.save(ordersGroup));
    }

    public List<OrdersGroupResponseDTO> findAll() {
        return ordersGroupMapper.toDTOList(ordersGroupRepository.findAll());
    }

    public OrdersGroupResponseDTO findById(Long id) {
        OrdersGroup ordersGroup = findOrdersGroupById(id);
        return ordersGroupMapper.toResponseDTO(ordersGroup);
    }

    @Transactional
    public OrdersGroupResponseDTO update(Long id,OrdersGroupRequestDTO ordersGroupRequestDTO) {
       OrdersGroup ordersGroup = findOrdersGroupById(id);
       setOrdersToOrdersGroup(ordersGroupRequestDTO, ordersGroup);
       ordersGroup = ordersGroupMapper.updateOrdersGroup(ordersGroupRequestDTO, ordersGroup);
       return ordersGroupMapper.toResponseDTO(ordersGroupRepository.save(ordersGroup));
    }

    private void setOrdersToOrdersGroup(OrdersGroupRequestDTO ordersGroupRequestDTO, OrdersGroup ordersGroup) {
        List<Order> orders = ordersGroupRequestDTO.getOrdersIds().stream()
                .map(orderService::findOrderById)
                .collect(Collectors.toList());

        if (orders.isEmpty()) {
            throw new BusinessException("É necessário ao menos um pedido para criar um grupo");
        }

        BigDecimal total = orders.stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer foodsTotalQuantity = orders.stream()
                .flatMap(order -> order.getOrderFoods().stream())
                        .map(OrderFood::getQuantity)
                                .reduce(0, Integer::sum);

        ordersGroup.setItemQuantity(foodsTotalQuantity);

        orders.forEach(order -> order.setOrdersGroup(ordersGroup));

        ordersGroup.getOrders().clear();
        ordersGroup.getOrders().addAll(orders);
        ordersGroup.setTotalPrice(total);
    }

    public void delete(Long id) {
        if (!ordersGroupRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format("Grupo de id %d não encontrado", id));
        }
        ordersGroupRepository.deleteById(id);
    }

    private OrdersGroup findOrdersGroupById(Long id) {
        return ordersGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Grupo de id %d não encontrado", id)));
    }
}
