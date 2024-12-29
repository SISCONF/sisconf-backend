package br.ifrn.edu.sisconf.service;

import org.springframework.beans.factory.annotation.Autowired;

import br.ifrn.edu.sisconf.mapper.StockMapper;
import br.ifrn.edu.sisconf.repository.StockRepository;

public class StockService {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private StockMapper stockMapper;
}
