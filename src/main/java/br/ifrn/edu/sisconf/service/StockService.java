package br.ifrn.edu.sisconf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.Stock;
import br.ifrn.edu.sisconf.domain.dtos.StockResponseDTO;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.StockMapper;
import br.ifrn.edu.sisconf.repository.EntrepreneurRepository;
import br.ifrn.edu.sisconf.repository.StockRepository;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private EntrepreneurRepository entrepreneurRepository;

    public void save(Entrepreneur entrepreneur) {
        Stock stock = new Stock();
        stock.setEntrepreneur(entrepreneur);
        stockRepository.save(stock);
    }

    public void deleteByEntrepreneurId(Long entrepreneurId) {
        Stock stock = stockRepository.findByEntrepreneurId(entrepreneurId);
        if (stock == null) {
            throw new ResourceNotFoundException("Este estoque não existe");
        }
        stockRepository.deleteById(stock.getId());
    }

    public StockResponseDTO getByEntrepreneurId(Long entrepreneurId) {
        Entrepreneur entrepreneur = entrepreneurRepository.findById(entrepreneurId).orElseThrow(() -> new ResourceNotFoundException("Este empreendedor não existe"));
        Stock stock = entrepreneur.getStock();
        return stockMapper.toResponseDTO(stock);
    }
}
