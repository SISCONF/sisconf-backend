package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.EntrepreneurMapper;
import br.ifrn.edu.sisconf.repository.EntrepreneurRepository;
import br.ifrn.edu.sisconf.service.keycloak.KeycloakUserService;
import br.ifrn.edu.sisconf.util.EntrepreneurTestUtil;

public class EntrepreneurServiceTest {
    @Mock
    private KeycloakUserService keycloakUserService;

    @Mock
    private EntrepreneurRepository entrepreneurRepository;

    @Mock
    private EntrepreneurMapper entrepreneurMapper;

    @Mock
    private StockService stockService;

    @InjectMocks
    private EntrepreneurService entrepreneurService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnEntrepreneurGetByIdWhenIdValid() {
        var entrepreneur = Instancio.create(Entrepreneur.class);
        entrepreneur.setId(1L);
        var expectedResponseDTO = EntrepreneurTestUtil.toResponseDTO(entrepreneur);
        when(entrepreneurRepository.findById(entrepreneur.getId())).thenReturn(
            Optional.of(entrepreneur)
        );
        when(entrepreneurMapper.toResponseDTO(entrepreneur)).thenReturn(
            expectedResponseDTO
        );

        var actualResponseDTO = entrepreneurService.getById(entrepreneur.getId());
        assertNotNull(actualResponseDTO);
        assertEquals(expectedResponseDTO, actualResponseDTO);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionGetByIdWhenIdInvalid() {
        when(entrepreneurRepository.findById(-1L)).thenReturn(Optional.empty());
        
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> entrepreneurService.getById(-1L)
        );
        assertEquals("Empreendedor com id -1 não existe", exception.getMessage());
    }

    @Test
    public void shouldSuccesfullyRemoveEntrepreneurDeleteByIdWhenIdValid() {
        var entrepreneur = Instancio.create(Entrepreneur.class);
        entrepreneur.setId(1L);
        when(entrepreneurRepository.findById(entrepreneur.getId())).thenReturn(
            Optional.of(entrepreneur)
        );

        entrepreneurService.deleteById(entrepreneur.getId());

        verify(stockService, times(1)).deleteByEntrepreneurId(entrepreneur.getId());
        verify(entrepreneurRepository, times(1)).deleteById(entrepreneur.getId());
        verify(keycloakUserService, times(1)).deleteById(
            entrepreneur.getPerson().getKeycloakId()
        );
    }

    @Test
    public void shouldNotRemoveEntrepreneurDeleteByIdWhenIdInvalid() {
        when(entrepreneurRepository.findById(-1L)).thenReturn(
            Optional.empty()
        );
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> entrepreneurService.deleteById(-1L)
        );

        assertEquals("Empreendedor com id -1 não existe", exception.getMessage());
        verify(stockService, never()).deleteByEntrepreneurId(-1L);
        verify(entrepreneurRepository, never()).deleteById(-1L);
        verify(keycloakUserService, never()).deleteById(
            null
        );
    }

    @Test
    public void shouldReturnListofEntrepreneurs() {
        var entrepreneur = Instancio.create(Entrepreneur.class);

        when(entrepreneurRepository.findAll()).thenReturn(
            List.of(entrepreneur)
        );
        when(entrepreneurMapper.toDTOList(List.of(entrepreneur))).thenReturn(
            List.of(EntrepreneurTestUtil.toResponseDTO(entrepreneur))
        );

        var actualEntrepreneurList = entrepreneurService.getAll();

        assertEquals(1, actualEntrepreneurList.size());
    }
}
