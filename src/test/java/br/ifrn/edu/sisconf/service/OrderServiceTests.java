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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.Order;
import br.ifrn.edu.sisconf.domain.dtos.Order.OrderRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Order.OrderResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Order.OrderUpdateRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderFoodRequestDTO;
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
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> 
            orderService.createOrder(customer.getId(), orderRequestDTO)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(customerRepository).findById(customer.getId());
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
        

        when(orderRepository.existsById(order.getId())).thenReturn(true);

        orderService.deleteOrder(order.getId());

        verify(orderRepository).existsById(order.getId());
        verify(orderRepository).deleteById(order.getId());
    }

    @Test
    @DisplayName("Should throw exception when order does not exist")
    public void shouldThrowExceptionWhenOrderDoesNotExist() {
        

        when(orderRepository.existsById(order.getId())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> orderService.deleteOrder(order.getId()));

        verify(orderRepository).existsById(order.getId());
        verify(orderRepository, never()).deleteById(order.getId());
    }


    @Test
    @DisplayName("Should update only the order status")
    public void shouldUpdateOrderStatusOnly() {
        OrderUpdateRequestDTO orderUpdateRequestDTO = new OrderUpdateRequestDTO();
        orderUpdateRequestDTO.setStatus(OrderStatus.ACCEPTED);
        orderUpdateRequestDTO.setFoodsQuantities(List.of());

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.updateOrder(order.getId(), orderUpdateRequestDTO);

        assertEquals(OrderStatus.ACCEPTED, order.getStatus());
        verify(orderRepository).findById(order.getId());
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("Should add new items to the order")
    public void shouldAddNewItemsToOrder() {
        OrderUpdateRequestDTO orderUpdateRequestDTO = new OrderUpdateRequestDTO();
        orderUpdateRequestDTO.setFoodsQuantities(List.of(new OrderFoodRequestDTO(food.getId(), 2)));

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(foodRepository.findAllById(List.of(food.getId()))).thenReturn(List.of(food));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.updateOrder(order.getId(), orderUpdateRequestDTO);

        assertEquals(1, order.getOrderFoods().size());
        assertEquals(food, order.getOrderFoods().get(0).getFood());
        assertEquals(2, order.getOrderFoods().get(0).getQuantity());

        verify(orderRepository).findById(order.getId());
        verify(foodRepository).findAllById(List.of(food.getId()));
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("Should correctly calculate the total price when adding new items")
    public void shouldCorrectlyCalculateTotalPriceWhenAddingNewItems() {
        BigDecimal initialPrice = food.getUnitPrice().multiply(BigDecimal.valueOf(2));
        BigDecimal additionalPrice = food.getUnitPrice().multiply(BigDecimal.valueOf(3));

        OrderUpdateRequestDTO orderUpdateRequestDTO = new OrderUpdateRequestDTO();
        orderUpdateRequestDTO.setFoodsQuantities(List.of(new OrderFoodRequestDTO(food.getId(), 2))); 
        orderUpdateRequestDTO.setStatus(OrderStatus.WAITING);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(foodRepository.findAllById(List.of(food.getId()))).thenReturn(List.of(food));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.updateOrder(order.getId(), orderUpdateRequestDTO);

        BigDecimal expectedTotalPrice = initialPrice.add(additionalPrice);
        assertEquals(expectedTotalPrice, order.getTotalPrice());

        verify(orderRepository).findById(order.getId());
        verify(foodRepository).findAllById(List.of(food.getId()));
        verify(orderRepository).save(order);
    }

}
