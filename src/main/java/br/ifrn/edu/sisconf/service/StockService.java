package br.ifrn.edu.sisconf.service;

import org.springframework.beans.factory.annotation.Autowired;

import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.Stock;
import br.ifrn.edu.sisconf.domain.dtos.StockResponseDTO;
import br.ifrn.edu.sisconf.mapper.StockMapper;
import br.ifrn.edu.sisconf.repository.StockRepository;

public class StockService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    public void save(Entrepreneur entrepreneur) {
        Stock stock = new Stock();
        stock.setEntrepreneur(entrepreneur);
        stockRepository.save(stock);
    }

    public void delete(Entrepreneur entrepreneur) {
        Stock stock = stockRepository.findByEntrepreneurId(entrepreneur.getId());
        stockRepository.deleteById(stock.getId());
    }

    public StockResponseDTO get(Entrepreneur entrepreneur) {
        Stock stock = entrepreneur.getStock();
        return stockMapper.toResponseDTO(stock);
    }
}
