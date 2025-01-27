package br.ifrn.edu.sisconf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ifrn.edu.sisconf.domain.Entrepreneur;
import br.ifrn.edu.sisconf.domain.Food;
import br.ifrn.edu.sisconf.domain.Stock;
import br.ifrn.edu.sisconf.domain.StockFood;
import br.ifrn.edu.sisconf.domain.dtos.StockFoodListRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.StockFoodRequestDTO;
import br.ifrn.edu.sisconf.domain.dtos.StockResponseDTO;
import br.ifrn.edu.sisconf.exception.BusinessException;
import br.ifrn.edu.sisconf.exception.ResourceNotFoundException;
import br.ifrn.edu.sisconf.mapper.StockMapper;
import br.ifrn.edu.sisconf.repository.EntrepreneurRepository;
import br.ifrn.edu.sisconf.repository.FoodRepository;
import br.ifrn.edu.sisconf.repository.StockFoodRepository;
import br.ifrn.edu.sisconf.repository.StockRepository;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private EntrepreneurRepository entrepreneurRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private StockFoodRepository stockFoodRepository;

    public Stock findStock(Long entrepreneurId) {
        return stockRepository.findByEntrepreneurId(entrepreneurId).orElseThrow(() -> new ResourceNotFoundException("Este estoque não existe"));
    }

    public void save(Entrepreneur entrepreneur) {
        Stock stock = new Stock();
        stock.setEntrepreneur(entrepreneur);
        stockRepository.save(stock);
    }

    public void deleteByEntrepreneurId(Long entrepreneurId) {
        Stock stock = findStock(entrepreneurId);
        stockRepository.deleteById(stock.getId());
    }

    public StockResponseDTO getByEntrepreneurId(Long entrepreneurId) {
        Entrepreneur entrepreneur = entrepreneurRepository.findById(entrepreneurId).orElseThrow(() -> new ResourceNotFoundException("Este empreendedor não existe"));
        Stock stock = entrepreneur.getStock();
        return stockMapper.toResponseDTO(stock);
    }

    public StockResponseDTO associateFoods(Long entrepreneurId, StockFoodRequestDTO stockFoodRequestDTO) {
        Stock stock = findStock(entrepreneurId);
        StockFood stockFood = new StockFood();
        stockFood.setStock(stock);
        
        for (StockFoodListRequestDTO foodItem : stockFoodRequestDTO.getFoods()) {
            Food food = foodRepository.findById(foodItem.getFoodId()).orElseThrow(() -> new ResourceNotFoundException("Essa comida não existe."));
            
            if (stock.getFoods().contains(food)) {
                throw new BusinessException("Esta comida já está no estoque");
            }
            stockFood.setFood(food);
            stockFood.setQuantity(foodItem.getQuantity());

            stockFoodRepository.save(stockFood);
        }

        return stockMapper.toResponseDTO(stock);
    }
}
