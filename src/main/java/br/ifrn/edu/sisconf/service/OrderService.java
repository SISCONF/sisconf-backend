package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.Order;
import br.ifrn.edu.sisconf.domain.OrderFood;
import br.ifrn.edu.sisconf.domain.dtos.OrderFoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderUpdateRequestDTO;
import br.ifrn.edu.sisconf.domain.enums.OrderStatus;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.OrderMapper;
import br.ifrn.edu.sisconf.repository.CustomerRepository;
import br.ifrn.edu.sisconf.repository.FoodRepository;
import br.ifrn.edu.sisconf.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private OrderMapper mapper;

    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Customer customer = customerRepository.findById(orderRequestDTO.getCustomerId())
                .orElseThrow(() -> new  ResourceNotFoundException("Cliente não encontrado"));

        List<Long> foodIds = orderRequestDTO.getFoodsQuantities().stream()
                .map(OrderFoodRequestDTO::getId)
                .collect(Collectors.toList());

        List<Food> foods = foodRepository.findAllById(foodIds);
        if (foods.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma comida válida encontrada");
        }

        BigDecimal totalPrice = orderRequestDTO.getFoodsQuantities().stream()
                .map(orderFoodRequest -> {
                    Food food = foods.stream()
                            .filter(f -> f.getId().equals(orderFoodRequest.getId()))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("Comida não encontrada para ID: " + orderFoodRequest.getId()));
                    return food.getUnitPrice().multiply(BigDecimal.valueOf(orderFoodRequest.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);



        Order order = mapper.toEntity(orderRequestDTO);
        order.setCustomer(customer);
        order.setCode(UUID.randomUUID());
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.WAITING);
        order.setOrderDate(LocalDateTime.now());

        List<OrderFood> orderFoods = orderRequestDTO.getFoodsQuantities().stream()
                .map(orderFoodRequest -> {
                    Food food = foods.stream()
                            .filter(f -> f.getId().equals(orderFoodRequest.getId()))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException("Comida não encontrada para ID: " + orderFoodRequest.getId()));
                    OrderFood orderFood = new OrderFood();
                    orderFood.setFood(food);
                    orderFood.setOrder(order);
                    orderFood.setQuantity(orderFoodRequest.getQuantity());
                    return orderFood;
                })
                .collect(Collectors.toList());

        order.getOrderFoods().addAll(orderFoods);

        return mapper.toResponseDTO(orderRepository.save(order));
    }

    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new  ResourceNotFoundException("Pedido com ID não encontrado"));
        return mapper.toResponseDTO(order);
    }

    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return mapper.toDTOList(orders);
    }

    public OrderResponseDTO updateOrder(Long id, OrderUpdateRequestDTO orderUpdateRequestDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        mapper.updateEntityFromDTO(orderUpdateRequestDTO, order);
        return mapper.toResponseDTO(orderRepository.save(order));
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido não encontrado.");
        }
        orderRepository.deleteById(id);
    }
}
