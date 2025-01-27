package br.ifrn.edu.sisconf.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.Stock;
import br.ifrn.edu.sisconf.domain.dtos.StockResponseDTO;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.StockMapper;
import br.ifrn.edu.sisconf.repository.EntrepreneurRepository;
import br.ifrn.edu.sisconf.repository.StockRepository;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {
    @InjectMocks
    private StockService stockService;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @Mock
    private EntrepreneurRepository entrepreneurRepository;

    @Test
    public void shouldCreateStockSuccessfully() {
        Entrepreneur entrepreneur = new Entrepreneur();
        entrepreneur.setId(1L);

        stockService.save(entrepreneur);

        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    public void shouldDeleteStockSuccessfully() {
        Long entrepreneurId = 1L;

        Stock stock = new Stock();
        stock.setId(1L);

        when(stockRepository.findByEntrepreneurId(entrepreneurId)).thenReturn(Optional.of(stock));

        stockService.deleteByEntrepreneurId(1L);

        verify(stockRepository).deleteById(stock.getId());
    }

    @Test
    public void shouldThrowErrorWhenDeletingUnexistingEntrepreneurStock() {
        Long entrepreneurId = 1L;

        when(stockRepository.findByEntrepreneurId(entrepreneurId)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> stockService.deleteByEntrepreneurId(entrepreneurId));

        assertEquals("Este estoque não existe", exception.getMessage());
    }

    @Test
    public void findEntrepreneurStockSuccessfully() {
        Long entrepreneurId = 1L;
        Entrepreneur entrepreneur = new Entrepreneur();
        entrepreneur.setId(entrepreneurId);

        Stock stock = new Stock();
        stock.setId(1L);
        entrepreneur.setStock(stock);

        StockResponseDTO stockResponseDTO = new StockResponseDTO();
        stockResponseDTO.setId(1L);
        stockResponseDTO.setEntrepreneurId(entrepreneurId);

        when(entrepreneurRepository.findById(entrepreneurId)).thenReturn(Optional.of(entrepreneur));
        when(stockMapper.toResponseDTO(stock)).thenReturn(stockResponseDTO);

        StockResponseDTO stockResult = stockService.getByEntrepreneurId(entrepreneurId);

        assertEquals(stockResponseDTO, stockResult);
    }

    @Test
    public void failedStockGetBecauseOfUnexistingEntrepreneur() {
        Long entrepreneurId = 1L;

        when(entrepreneurRepository.findById(entrepreneurId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> stockService.getByEntrepreneurId(entrepreneurId));

        assertEquals("Este empreendedor não existe", exception.getMessage());
    }
}
