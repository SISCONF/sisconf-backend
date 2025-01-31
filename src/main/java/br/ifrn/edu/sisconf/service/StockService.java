package br.ifrn.edu.sisconf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public StockFood findStockFoodByStockIdAndFoodId(Long stockId, Long foodId) {
        return stockFoodRepository.findByStockIdAndFoodId(stockId, foodId).orElseThrow(() -> new ResourceNotFoundException("Essa comida não está nesse estoque"));
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
        
        List<Long> foodsIds = stockFoodRequestDTO.getFoods().stream()
                .map(StockFoodListRequestDTO::getFoodId)
                .collect(Collectors.toList());

        List<Food> foundFoods = foodRepository.findAllById(foodsIds);
        Map<Long, Food> foodMap = foundFoods.stream().collect(Collectors.toMap(Food::getId, food -> food));

        if (foundFoods.size() != foodsIds.size()) {
            throw new ResourceNotFoundException("Uma ou mais das comidas informadas não foi encontrada.");
        }

        List<StockFood> foodsFromStock = new ArrayList<>();
        for (StockFoodListRequestDTO foodItem : stockFoodRequestDTO.getFoods()) {
            Food food = foodMap.get(foodItem.getFoodId());
            
            if (stock.getFoodsEntities().contains(food)) {
                throw new BusinessException("A comida com ID " + foodItem.getFoodId() + " já está no estoque.");
            }

            StockFood stockFood = new StockFood();
            stockFood.setStock(stock);
            stockFood.setFood(food);
            stockFood.setQuantity(foodItem.getQuantity());
            foodsFromStock.add(stockFood);
        }

        stockFoodRepository.saveAll(foodsFromStock);

        return stockMapper.toResponseDTO(stock);
    }

    public void updateStockFoodQuantity(Long entrepreneurId, StockFoodRequestDTO stockFoodRequestDTO) {
        Stock stock = findStock(entrepreneurId);
        
        List<StockFood> stockFoodsToBeUpdated = new ArrayList<>();
        for (StockFoodListRequestDTO foodItem : stockFoodRequestDTO.getFoods()) {
            if (!foodRepository.existsById(foodItem.getFoodId())) {
                throw new ResourceNotFoundException("Comida não encontrada");
            }
            StockFood stockFood = findStockFoodByStockIdAndFoodId(stock.getId(), foodItem.getFoodId());

            stockFood.setQuantity(foodItem.getQuantity());
            stockFoodsToBeUpdated.add(stockFood);
        }

        stockFoodRepository.saveAll(stockFoodsToBeUpdated);
    }

    public void removeFoodFromStock(Long entrepreneurId, Long foodId) {
        Stock stock = findStock(entrepreneurId);
        if (!foodRepository.existsById(foodId)) {
            throw new ResourceNotFoundException("Comida não encontrada");
        }

        StockFood stockFood = findStockFoodByStockIdAndFoodId(stock.getId(), foodId);
        stockFoodRepository.deleteById(stockFood.getId());
    }
}
