package br.ifrn.edu.sisconf.controller;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ifrn.edu.sisconf.SecurityTestConfig;
import br.ifrn.edu.sisconf.constants.KeycloakConstants;
import br.ifrn.edu.sisconf.domain.Address;
import br.ifrn.edu.sisconf.domain.City;
import br.ifrn.edu.sisconf.domain.Customer;
import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.Order;
import br.ifrn.edu.sisconf.domain.Person;
import br.ifrn.edu.sisconf.domain.dtos.OrderFoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrderFoodResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Order.OrderRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.Order.OrderResponseDTO;
import br.ifrn.edu.sisconf.domain.dtos.Order.OrderUpdateRequestDTO;
import br.ifrn.edu.sisconf.domain.enums.CustomerCategory;
import br.ifrn.edu.sisconf.domain.enums.OrderFoodQuantityType;
import br.ifrn.edu.sisconf.domain.enums.OrderStatus;
import br.ifrn.edu.sisconf.mapper.OrderMapper;
import br.ifrn.edu.sisconf.repository.CityRepository;
import br.ifrn.edu.sisconf.repository.CustomerRepository;
import br.ifrn.edu.sisconf.repository.EntrepreneurRepository;
import br.ifrn.edu.sisconf.repository.FoodRepository;
import br.ifrn.edu.sisconf.repository.OrderRepository;
import br.ifrn.edu.sisconf.security.SisconfUserDetails;
import br.ifrn.edu.sisconf.service.OrderService;
import br.ifrn.edu.sisconf.util.JwtTestUtil;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(SecurityTestConfig.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTestUtil jwtTestUtil;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntrepreneurRepository entrepreneurRepository;

    @Autowired
    private CityRepository cityRepository;

    private Order order;
    private OrderRequestDTO orderRequestDTO;
    private List<OrderFoodRequestDTO> orderFoodRequestDTOs;
    private Customer customer;

    @BeforeEach
    public void setUp() {
        Food firstFood = foodRepository.findById(1L).orElseThrow();
        Food secondFood = foodRepository.findById(2L).orElseThrow();

        orderFoodRequestDTOs = List.of(
            new OrderFoodRequestDTO(
                firstFood.getId(),
                10,
                OrderFoodQuantityType.KG
            ),
            new OrderFoodRequestDTO(
                secondFood.getId(),
                20,
                OrderFoodQuantityType.CX
            )
        );
        orderRequestDTO = new OrderRequestDTO(
            orderFoodRequestDTOs  
        );
        
        City city = cityRepository.findAll().getFirst();
        customer = new Customer(
            CustomerCategory.MARKETER,
            new Person(
                UUID.randomUUID().toString(),
                "Nome Teste",
                "Segundo Nome Teste",
                "teste@email.com",
                "111.111.111-11",
                null,
                "(11) 91111-1111",
                new Address(
                    "Nome da Rua",
                    "11111-111",
                    "Nome Bairro",
                    22,
                    city
                ),
                null,
                null
            ),
            null
        );
        customer = customerRepository.save(customer);
        
        SisconfUserDetails userDetails = new SisconfUserDetails(
            customer.getPerson().getKeycloakId(),
            customer.getPerson().getEmail(),
            customer.getPerson().getEmail(),
            new ArrayList<>()
        );

        var orderResponseDTO = orderService.createOrder(userDetails, orderRequestDTO);
        order = orderRepository.findById(orderResponseDTO.getId()).orElseThrow();
    }

    @AfterEach
    public void tearDown() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
        entrepreneurRepository.deleteAll();
    }

    @Test
    public void shouldReturnListOfOrders() throws Exception {
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        String expectedResponse = objectMapper.writeValueAsString(
            orderMapper.toDTOList(List.of(order))
        );

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/orders", new ArrayList<>())
            .header("Authorization", "Bearer " + tokenString)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }

    @Test
    public void shouldReturnEmptyListOfOrdersWhenInvalidKeycloakId() throws Exception {
        customer.getPerson().setKeycloakId("");
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/orders", new ArrayList<>())
            .header("Authorization", "Bearer " + tokenString)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }

    @Test
    public void shouldReturnListOfAllOrdersWhenRequestUserIsEntrepreneur() throws Exception {
        var firstFood = foodRepository.findById(1L).orElseThrow();
        var secondFood = foodRepository.findById(2L).orElseThrow();
        var anotherCustomer = customer = new Customer(
            CustomerCategory.MARKETER,
            new Person(
                UUID.randomUUID().toString(),
                "Nome Teste",
                "Segundo Nome Teste",
                "teste2@email.com",
                "222.222.222-22",
                null,
                "(11) 94444-4444",
                new Address(
                    "Nome da Rua",
                    "99999-999",
                    "Nome Bairro",
                    22,
                    cityRepository.findAll().getFirst()
                ),
                null,
                null
            ),
            null
        );
        anotherCustomer = customerRepository.save(anotherCustomer);
        var anotherUserDetails = new SisconfUserDetails(
            anotherCustomer.getPerson().getKeycloakId(),
            anotherCustomer.getPerson().getEmail(),
            anotherCustomer.getPerson().getEmail(),
            new ArrayList<>()
        );
        var anotherOrderRequestDTO = new OrderRequestDTO(
            List.of(
                new OrderFoodRequestDTO(
                    firstFood.getId(),
                    10,
                    OrderFoodQuantityType.KG
                ),
                new OrderFoodRequestDTO(
                    secondFood.getId(),
                    20,
                    OrderFoodQuantityType.CX
                )
            )
        );
        var anotherOrderResponseDTO = orderService.createOrder(anotherUserDetails, anotherOrderRequestDTO);
        var anotherOrder = orderRepository.findById(anotherOrderResponseDTO.getId()).orElseThrow();

        var entrepreneur = new Entrepreneur(
            "Meu Negócio Teste",
            new Person(
                UUID.randomUUID().toString(),
                "Nome Teste",
                "Segundo Nome Teste",
                "teste3@email.com",
                "333.333.333-33",
                "33.333.333/3333-11",
                "(11) 93333-3333",
                new Address(
                    "Nome da Rua",
                    "99999-999",
                    "Nome Bairro",
                    22,
                    cityRepository.findAll().getFirst()
                ),
                null,
                null
            ),
            null
        );
        entrepreneur = entrepreneurRepository.save(entrepreneur);

        String tokenString = jwtTestUtil.getToken(entrepreneur.getPerson().getEmail());
        String roleName = KeycloakConstants.ROLE_LIST_ALL_ORDERS.split("'")[1];
        Jwt jwt = jwtTestUtil.getJwt(tokenString, entrepreneur.getPerson(), List.of(roleName));
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        String expectedResponse = objectMapper.writeValueAsString(
            orderMapper.toDTOList(List.of(anotherOrder, order))
        );

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/orders", new ArrayList<>())
            .header("Authorization", "Bearer " + tokenString)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
        .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }

    @Test
    public void shouldCreateOrderWhenRequestDataValid() throws Exception {
        orderRepository.deleteAll();
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);
        
        String requestBody = objectMapper.writeValueAsString(
            orderRequestDTO
        );

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/orders", new ArrayList<>())
            .header("Authorization", "Bearer " + tokenString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void shouldReturnBadRequestWhenFoodIdNullOrderCreate() throws Exception {
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        orderFoodRequestDTOs.get(0).setFoodId(null);
        orderFoodRequestDTOs.get(0).setQuantityType(OrderFoodQuantityType.KG);
        orderFoodRequestDTOs.get(0).setQuantity(10);
        String requestBody = objectMapper.writeValueAsString(
            orderFoodRequestDTOs
        );

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/orders", new ArrayList<>())
            .header("Authorization", "Bearer " + tokenString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenFoodIdInvalidOrderCreate() throws Exception {
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        orderFoodRequestDTOs.get(0).setFoodId(0L);
        orderFoodRequestDTOs.get(0).setQuantityType(OrderFoodQuantityType.KG);
        orderFoodRequestDTOs.get(0).setQuantity(10);
        String requestBody = objectMapper.writeValueAsString(
            orderFoodRequestDTOs
        );

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/orders", new ArrayList<>())
            .header("Authorization", "Bearer " + tokenString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenQuantityTypeNullOrderCreate() throws Exception {
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        orderFoodRequestDTOs.get(0).setQuantityType(null);
        orderFoodRequestDTOs.get(0).setQuantity(10);
        String requestBody = objectMapper.writeValueAsString(
            orderFoodRequestDTOs
        );

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/orders", new ArrayList<>())
            .header("Authorization", "Bearer " + tokenString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenFoodQuantityNullOrderCreate() throws Exception {
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        orderFoodRequestDTOs.get(0).setQuantityType(OrderFoodQuantityType.KG);
        orderFoodRequestDTOs.get(0).setQuantity(null);
        String requestBody = objectMapper.writeValueAsString(
            orderFoodRequestDTOs
        );

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/orders", new ArrayList<>())
            .header("Authorization", "Bearer " + tokenString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenFoodQuantityInvalidOrderCreate() throws Exception {
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        orderFoodRequestDTOs.get(0).setQuantityType(OrderFoodQuantityType.KG);
        orderFoodRequestDTOs.get(0).setQuantity(0);
        String requestBody = objectMapper.writeValueAsString(
            orderFoodRequestDTOs
        );

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/orders", new ArrayList<>())
            .header("Authorization", "Bearer " + tokenString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundWhenCustomerKeycloakIdInvalidOrderCreate() throws Exception {
        customer.getPerson().setKeycloakId("");
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);
        
        String requestBody = objectMapper.writeValueAsString(
            orderRequestDTO
        );

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/orders", new ArrayList<>())
            .header("Authorization", "Bearer " + tokenString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnBadRequestWhenFoodsListEmpty() throws Exception {
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        orderRequestDTO.setFoodsQuantities(List.of());
        String requestBody = objectMapper.writeValueAsString(
            orderRequestDTO
        );

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/orders", new ArrayList<>())
            .header("Authorization", "Bearer " + tokenString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldReturnOrderWhenGetByExistingAndValidId() throws Exception {
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        String expectedResponse = objectMapper.writeValueAsString(
            orderMapper.toResponseDTO(order)      
        );

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/orders/{id}", order.getId())
            .header("Authorization", "Bearer " + tokenString)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }

    @Test
    public void shouldReturnNotFoundWhenGetByUnexistingAndInvalidId() throws Exception {
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/orders/{id}", -1L)
            .header("Authorization", "Bearer " + tokenString)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldDeleteOrderWhenDeleteByExistingAndValidId() throws Exception {
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/orders/{id}", order.getId())
            .header("Authorization", "Bearer " + tokenString)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundWhenInvalidOrderIdUpdate() throws Exception {
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        Food anotherFood = foodRepository.findById(10L).orElseThrow();
        Food yetAnotherFood = foodRepository.findById(11L).orElseThrow();
        var updateOrderFoodRequestDTO = List.of(
            new OrderFoodRequestDTO(
                anotherFood.getId(),
                30,
                OrderFoodQuantityType.KG
            ),
            new OrderFoodRequestDTO(
                yetAnotherFood.getId(),
                40,
                OrderFoodQuantityType.CX
            )
        );
        orderRequestDTO = new OrderRequestDTO(
            updateOrderFoodRequestDTO  
        );
        String requestBody = objectMapper.writeValueAsString(
            orderRequestDTO
        );

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/orders/{id}", -1L)
            .header("Authorization", "Bearer " + tokenString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldUpdateOrderWhenValidRequestBody() throws Exception {
        String tokenString = jwtTestUtil.getToken(customer.getPerson().getEmail());
        Jwt jwt = jwtTestUtil.getJwt(tokenString, customer.getPerson(), new ArrayList<>());
        when(jwtDecoder.decode(tokenString)).thenReturn(jwt);

        Food anotherFood = foodRepository.findById(10L).orElseThrow();
        Food yetAnotherFood = foodRepository.findById(11L).orElseThrow();
        var updateOrderFoodRequestDTO = List.of(
            new OrderFoodRequestDTO(
                anotherFood.getId(),
                30,
                OrderFoodQuantityType.KG
            ),
            new OrderFoodRequestDTO(
                yetAnotherFood.getId(),
                40,
                OrderFoodQuantityType.CX
            )
        );
        var orderUpdateRequestDTO = new OrderUpdateRequestDTO(
            OrderStatus.ACCEPTED,
            updateOrderFoodRequestDTO
        );
        String requestBody = objectMapper.writeValueAsString(
            orderUpdateRequestDTO
        );

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(
            order.getId(),
            order.getCode(),
            BigDecimal.valueOf(30L)
                .multiply(anotherFood.getUnitPrice())
                .add(
                    BigDecimal.valueOf(40L)
                    .multiply(yetAnotherFood.getUnitPrice())
                ),
            OrderStatus.ACCEPTED,
            order.getOrderDate(),
            order.getCustomer().getId(),
            List.of(
                new OrderFoodResponseDTO(
                    yetAnotherFood.getId(),
                    yetAnotherFood.getName(),
                    yetAnotherFood.getUnitPrice(),
                    40,
                    yetAnotherFood.getCategory()
                ),
                new OrderFoodResponseDTO(
                    anotherFood.getId(),
                    anotherFood.getName(),
                    anotherFood.getUnitPrice(),
                    30,
                    anotherFood.getCategory()
                )
            )
        );
        String expectedResponse = objectMapper.writeValueAsString(orderResponseDTO);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/orders/{id}", order.getId())
            .header("Authorization", "Bearer " + tokenString)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(expectedResponse));
    }
}
