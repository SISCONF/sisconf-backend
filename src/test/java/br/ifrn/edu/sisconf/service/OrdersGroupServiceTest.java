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
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrdersGroupServiceTest {
    @Mock
    private OrdersGroupRepository ordersGroupRepository;

    @Mock
    private OrdersGroupMapper ordersGroupMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderService orderService;

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
        OrdersGroup ordersGroup = Instancio.create(OrdersGroup.class);
        OrdersGroupResponseDTO ordersGroupResponseDTO = Instancio.create(OrdersGroupResponseDTO.class);
        Order order = Instancio.create(Order.class);

        when(ordersGroupMapper.toEntity(ordersGroupRequestDTO)).thenReturn(ordersGroup);
        when(ordersGroupRepository.save(ordersGroup)).thenReturn(ordersGroup);
        when(ordersGroupMapper.toResponseDTO(ordersGroup)).thenReturn(ordersGroupResponseDTO);
        when(ordersGroupRepository.findById(any())).thenReturn(Optional.of(ordersGroup));
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        OrdersGroupResponseDTO savedOrdersGroup = ordersGroupService.save(ordersGroupRequestDTO);

        verify(ordersGroupRepository).save(any(OrdersGroup.class));
        assertEquals(ordersGroupResponseDTO, savedOrdersGroup);

    }

    @Test
    @DisplayName("Tenta criar um grupo com pedidos vazios e retorna erro")
    public void testCreateOrdersGroupWithEmptyOrdersShouldThrowException() {
        OrdersGroupRequestDTO ordersGroupRequestDTO = Instancio.of(OrdersGroupRequestDTO.class)
                .supply(field("ordersIds"), () -> new ArrayList<>())
                .create();

        OrdersGroup ordersGroup = Instancio.create(OrdersGroup.class);
        OrdersGroupResponseDTO ordersGroupResponseDTO = Instancio.create(OrdersGroupResponseDTO.class);
        Order order = Instancio.create(Order.class);

        when(ordersGroupMapper.toEntity(ordersGroupRequestDTO)).thenReturn(ordersGroup);
        when(ordersGroupMapper.toResponseDTO(ordersGroup)).thenReturn(ordersGroupResponseDTO);
        when(ordersGroupRepository.findById(any())).thenReturn(Optional.of(ordersGroup));
        when(orderService.findOrderById(any())).thenReturn(order);

        assertThrows(BusinessException.class, () -> ordersGroupService.save(ordersGroupRequestDTO));

        verify(ordersGroupRepository, never()).save(ordersGroup);
    }

    @Test
    @DisplayName("Lista todos os grupos de pedidos com sucesso")
    public void testListOrdersGroupSuccessfully() {
        OrdersGroup ordersGroup = Instancio.create(OrdersGroup.class);
        when(ordersGroupRepository.findAll()).thenReturn(List.of(ordersGroup));
        when(ordersGroupMapper.toDTOList(anyList())).thenReturn(List.of(new OrdersGroupResponseDTO()));

        List<OrdersGroupResponseDTO> result = ordersGroupService.findAll();

        verify(ordersGroupMapper).toDTOList(List.of(ordersGroup));
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Busca um grupo de pedidos pelo id com sucesso")
    public void testSearchOrdersGroupByIdSuccessfully() {
        Long validId = 1L;
        OrdersGroup ordersGroup = Instancio.create(OrdersGroup.class);
        OrdersGroupResponseDTO ordersGroupResponseDTO = Instancio.create(OrdersGroupResponseDTO.class);
        when(ordersGroupRepository.findById(validId)).thenReturn(Optional.of(ordersGroup));
        when(ordersGroupMapper.toResponseDTO(ordersGroup)).thenReturn(ordersGroupResponseDTO);

        OrdersGroupResponseDTO result = ordersGroupService.findById(validId);
        verify(ordersGroupRepository).findById(validId);
        verify(ordersGroupMapper).toResponseDTO(ordersGroup);
        assertEquals(ordersGroupResponseDTO, result);
    }

    @Test
    @DisplayName("Busca um grupo de pedidos com id inexistente")
    public void testSearchOrdersGroupWithInvalidIdShouldThrowException() {
        Long invalidId = 999L;
        OrdersGroup ordersGroup = Instancio.create(OrdersGroup.class);
        OrdersGroupResponseDTO ordersGroupResponseDTO = Instancio.create(OrdersGroupResponseDTO.class);
        when(ordersGroupRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> ordersGroupService.findById(invalidId));
    }

    @Test
    @DisplayName("Atualiza um grupo de pedidos com sucesso")
    public void testUpdateOrdersGroupSuccessfully() {
        OrdersGroup ordersGroup = Instancio.create(OrdersGroup.class);
        OrdersGroupRequestDTO ordersGroupRequestDTO = Instancio.create(OrdersGroupRequestDTO.class);
        OrdersGroupResponseDTO ordersGroupResponseDTO = Instancio.create(OrdersGroupResponseDTO.class);
        Order order = Instancio.create(Order.class);
        when(ordersGroupRepository.findById(1L)).thenReturn(Optional.of(ordersGroup));
        when(ordersGroupMapper.updateOrdersGroup(ordersGroupRequestDTO, ordersGroup)).thenReturn(ordersGroup);
        when(ordersGroupMapper.toResponseDTO(ordersGroup)).thenReturn(ordersGroupResponseDTO);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        when(ordersGroupRepository.save(ordersGroup)).thenReturn(ordersGroup);


        OrdersGroupResponseDTO result = ordersGroupService.update(1L, ordersGroupRequestDTO);

        verify(ordersGroupRepository).save(ordersGroup);
        assertEquals(result, ordersGroupResponseDTO);
    }

    @Test
    @DisplayName("Excluir grupo de pedido com sucesso")
    public void testDeleteOrdersGroupSuccessfully() {
        OrdersGroup ordersGroup = Instancio.create(OrdersGroup.class);
        when(ordersGroupRepository.existsById(any())).thenReturn(true);

        ordersGroupService.delete(1L);
        verify(ordersGroupRepository).existsById(any());

    }

    @Test
    @DisplayName("Tentar excluir grupo de pedido com id inexistente")
    public void testDeleteOrdersGroupWithInvalidIdShouldThrowException() {
        Long invalidId = 9999L;
        when(ordersGroupRepository.existsById(any())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> ordersGroupService.delete(invalidId));
        verify(ordersGroupRepository, never()).deleteById(invalidId);

    }
}