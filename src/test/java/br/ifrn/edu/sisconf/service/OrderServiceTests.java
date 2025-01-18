package br.ifrn.edu.sisconf.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.Order;
import br.ifrn.edu.sisconf.domain.dtos.OrderFoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderUpdateRequestDTO;
import br.ifrn.edu.sisconf.domain.enums.FoodCategory;
import br.ifrn.edu.sisconf.domain.enums.OrderStatus;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.OrderMapper;
import br.ifrn.edu.sisconf.repository.CustomerRepository;
import br.ifrn.edu.sisconf.repository.FoodRepository;
import br.ifrn.edu.sisconf.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private FoodRepository foodRepository;

    private Customer customer;

    private Food food;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);

        food = new Food();
        food.setId(1L);
        food.setName("Goiaba Verde CX");
        food.setCategory(FoodCategory.FRUIT);
        food.setUnitPrice(new BigDecimal("100.00"));

        order = new Order();
        order.setId(1L);
    }

    @Test
    @DisplayName("Should return a message indicating that the user was not found")
    public void shouldThrowExceptionWhenCustomerNotFound() {
        Long customerId = 1L;
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> 
            orderService.createOrder(customerId, orderRequestDTO)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(customerRepository).findById(customerId);
    }

    @Test
    @DisplayName("Should create a new order with success")
    public void shouldCreateOrderWithValidInputs() {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setFoodsQuantities(List.of(new OrderFoodRequestDTO(1L, 2)));

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(foodRepository.findAllById(List.of(1L))).thenReturn(List.of(food));
        when(orderMapper.toEntity(orderRequestDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toResponseDTO(order)).thenReturn(orderResponseDTO);
        
        OrderResponseDTO createdOrder = orderService.createOrder(customer.getId(), orderRequestDTO);

        verify(orderRepository).save(order);
        assertEquals(orderResponseDTO, createdOrder);
    }

    @Test
    @DisplayName("should return the order list whether it is empty or not")
    public void shouldReturnAllOrders() {
        List<Order> orders = List.of(order);

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        List<OrderResponseDTO> orderResponseDTOs = List.of(orderResponseDTO);

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.toDTOList(orders)).thenReturn(orderResponseDTOs);
        
        List<OrderResponseDTO> allOrders = orderService.getAllOrders();

        assertEquals(orderResponseDTOs, allOrders);
        verify(orderRepository).findAll();
    }

    @Test
    @DisplayName("Should return the order by id when it exists")
    public void shouldReturnOrderByIdWhenExists() {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(1L);
     
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderMapper.toResponseDTO(order)).thenReturn(orderResponseDTO);

        OrderResponseDTO fetchOrder = orderService.getOrderById(order.getId());

        assertEquals(orderResponseDTO, fetchOrder);
        verify(orderRepository).findById(order.getId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when the food does not exist")
    public void shouldThrowExceptionWhenFoodNotFound() {
        OrderUpdateRequestDTO orderUpdateRequestDTO = new OrderUpdateRequestDTO();
        orderUpdateRequestDTO.setStatus(OrderStatus.WAITING);
        orderUpdateRequestDTO.setFoodsQuantities(List.of(new OrderFoodRequestDTO(999L, 3))); 

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(foodRepository.findAllById(List.of(999L))).thenReturn(List.of());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> 
            orderService.updateOrder(order.getId(), orderUpdateRequestDTO)
        );

        assertTrue(exception.getMessage().contains("IDs de comidas inválidos"));
        verify(orderRepository).findById(order.getId());
    }

    @Test
    @DisplayName("Should delete order when it exists")
    public void shouldDeleteOrderSuccessfully() {
        Long orderId = 1L;

        when(orderRepository.existsById(orderId)).thenReturn(true);

        orderService.deleteOrder(orderId);

        verify(orderRepository).existsById(orderId);
        verify(orderRepository).deleteById(orderId);
    }

}
