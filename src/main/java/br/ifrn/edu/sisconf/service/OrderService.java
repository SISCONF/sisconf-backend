package br.ifrn.edu.sisconf.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.Order;
import br.ifrn.edu.sisconf.domain.OrderFood;
import br.ifrn.edu.sisconf.domain.dtos.OrderFoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Order.OrderRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Order.OrderResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Order.OrderUpdateRequestDTO;
import br.ifrn.edu.sisconf.domain.enums.OrderStatus;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.OrderMapper;
import br.ifrn.edu.sisconf.repository.CustomerRepository;
import br.ifrn.edu.sisconf.repository.FoodRepository;
import br.ifrn.edu.sisconf.repository.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private OrderMapper orderMapper;

    private Map<Long, Food> fetchAndValidateFoods(List<OrderFoodRequestDTO> foodsQuantities) {
        List<Long> foodIds = foodsQuantities.stream()
            .map(OrderFoodRequestDTO::getFoodId)
            .distinct()
            .collect(Collectors.toList());

        List<Food> foods = foodRepository.findAllById(foodIds);

        if (foods.size() != foodIds.size()) {
            throw new ResourceNotFoundException("IDs de comidas inválidos: " + 
                foodIds.stream().filter(id -> foods.stream().noneMatch(f -> f.getId().equals(id))).collect(Collectors.toList()));
        }

        return foods.stream().collect(Collectors.toMap(Food::getId, food -> food));
    }

    public OrderResponseDTO createOrder(Long customerId, OrderRequestDTO orderRequestDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Map<Long, Food> foodMap = fetchAndValidateFoods(orderRequestDTO.getFoodsQuantities());

        BigDecimal totalPrice = orderRequestDTO.getFoodsQuantities().stream()
            .map(orderFoodRequest -> {
                Food food = foodMap.get(orderFoodRequest.getFoodId());
                return food.getUnitPrice().multiply(BigDecimal.valueOf(orderFoodRequest.getQuantity()));
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = orderMapper.toEntity(orderRequestDTO);
        order.setCustomer(customer);
        order.setCode(UUID.randomUUID());
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.WAITING);
        order.setOrderDate(LocalDateTime.now());

        List<OrderFood> orderFoods = orderRequestDTO.getFoodsQuantities().stream()
            .map(orderFoodRequest -> {
                Food food = foodMap.get(orderFoodRequest.getFoodId());
                OrderFood orderFood = new OrderFood();
                orderFood.setFood(food);
                orderFood.setOrder(order);
                orderFood.setQuantity(orderFoodRequest.getQuantity());
                return orderFood;
            })
            .collect(Collectors.toList());

        order.getOrderFoods().addAll(orderFoods);

        return orderMapper.toResponseDTO(orderRepository.save(order));
    }

    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new  ResourceNotFoundException("Pedido com ID não encontrado"));
        return orderMapper.toResponseDTO(order);
    }

    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toDTOList(orders);
    }

    public OrderResponseDTO updateOrder(Long id, OrderUpdateRequestDTO orderUpdateRequestDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        Map<Long, Food> foodMap = fetchAndValidateFoods(orderUpdateRequestDTO.getFoodsQuantities());

        for (OrderFoodRequestDTO orderFoodRequest : orderUpdateRequestDTO.getFoodsQuantities()) {
            Food food = foodMap.get(orderFoodRequest.getFoodId());
            
            // Verifica se o alimento já está no pedido
            boolean foodExistsInOrder = order.getOrderFoods().stream()
                    .anyMatch(orderFood -> orderFood.getFood().getId().equals(food.getId()));

            if (foodExistsInOrder) {
                // Se o alimento já existe, atualize a quantidade
                order.getOrderFoods().stream()
                    .filter(orderFood -> orderFood.getFood().getId().equals(food.getId()))
                    .forEach(orderFood -> orderFood.setQuantity(orderFood.getQuantity() + orderFoodRequest.getQuantity()));
            } else {
                // Caso contrário, adicione um novo alimento
                OrderFood orderFood = new OrderFood();
                orderFood.setFood(food);
                orderFood.setOrder(order);
                orderFood.setQuantity(orderFoodRequest.getQuantity());
                order.getOrderFoods().add(orderFood);
            }
        }

        BigDecimal totalPrice = order.getOrderFoods().stream()
            .map(orderFood -> orderFood.getFood().getUnitPrice().multiply(BigDecimal.valueOf(orderFood.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(totalPrice);

        orderMapper.updateEntityFromDTO(orderUpdateRequestDTO, order);
 
        return orderMapper.toResponseDTO(orderRepository.save(order));
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido não encontrado.");
        }
        orderRepository.deleteById(id);
    }
}
