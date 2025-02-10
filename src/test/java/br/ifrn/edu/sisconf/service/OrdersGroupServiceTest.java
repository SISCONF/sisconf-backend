package br.ifrn.edu.sisconf.service;

import br.ifrn.edu.sisconf.domain.Order;
import br.ifrn.edu.sisconf.domain.OrdersGroup;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.OrdersGroup.OrdersGroupResponseDTO;
import br.ifrn.edu.sisconf.domain.enums.OrdersGroupStatus;
import br.ifrn.edu.sisconf.mapper.OrdersGroupMapper;
import br.ifrn.edu.sisconf.repository.OrderRepository;
import br.ifrn.edu.sisconf.repository.OrdersGroupRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdersGroupServiceTest {
    @Mock
    private OrdersGroupRepository ordersGroupRepository;

    @Mock
    private OrdersGroupMapper ordersGroupMapper;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrdersGroupService ordersGroupService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Cria um novo grupo de pedidos com sucesso")
    public void testCreateOrdersGroupSuccessfully() {
        OrdersGroupRequestDTO ordersGroupRequestDTO = Instancio.create(OrdersGroupRequestDTO.class);
        OrdersGroup ordersGroup = spy(Instancio.create(OrdersGroup.class));
        OrdersGroupResponseDTO ordersGroupResponseDTO = Instancio.create(OrdersGroupResponseDTO.class);
        Order order = Instancio.create(Order.class);

        when(ordersGroupMapper.toEntity(ordersGroupRequestDTO)).thenReturn(ordersGroup);
        when(ordersGroupRepository.save(ordersGroup)).thenReturn(ordersGroup);
        when(ordersGroupMapper.toResponseDTO(ordersGroup)).thenReturn(ordersGroupResponseDTO);
        when(ordersGroupRepository.findById(any())).thenReturn(Optional.of(ordersGroup));
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        OrdersGroupResponseDTO savedOrdersGroup = ordersGroupService.save(ordersGroupRequestDTO);

        verify(ordersGroup).setCurrentStatus(OrdersGroupStatus.PLACED);
        assertEquals(ordersGroupResponseDTO, savedOrdersGroup);

    }

}