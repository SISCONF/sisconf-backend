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
import br.ifrn.edu.sisconf.domain.dtos.StockFoodDeleteRequestDTO;
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
import br.ifrn.edu.sisconf.security.SisconfUserDetails;

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

    @Autowired
    private PersonService personService;

    public Stock findStock(Long entrepreneurId) {
        return stockRepository.findByEntrepreneurId(entrepreneurId).orElseThrow(() -> new ResourceNotFoundException("Este estoque não existe"));
    }

    public StockFood findStockFoodByStockIdAndFoodId(Long stockId, Long foodId) {
        return stockFoodRepository.findByStockIdAndFoodId(stockId, foodId).orElseThrow(() -> new ResourceNotFoundException("Essa comida não está nesse estoque"));
    }

    public void throwIfNotEveryFoodIsFound(List<Long> foodsIds, List<Food> foundFoods) {
        if (foodsIds.size() != foundFoods.size()) {
            throw new ResourceNotFoundException("Uma ou mais comidas informadas não foram encontradas.");
        }
    }

    public List<Long> getFoodsIds(StockFoodRequestDTO stockFoodRequestDTO) {
        return stockFoodRequestDTO.getFoods().stream()
        .map(StockFoodListRequestDTO::getFoodId)
        .collect(Collectors.toList());
    }

    public Map<Long, Food> createFoodMap(List<Food> foods) {
        return foods.stream().collect(Collectors.toMap(Food::getId, food -> food));
    }

    public Entrepreneur findEntrepreneurById(Long id) {
        return entrepreneurRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Empreendedor não encontrado."));
    }

    public Map<Long, StockFood> createStockFoodMap(List<StockFood> stockFoods) {
        return stockFoods.stream()
                .collect(Collectors.toMap(stockFood -> stockFood.getFood().getId(), stockFood -> stockFood));
    }

    public void throwIfStockFoodIsNotFoundInStock(StockFood stockFood, Long foodId) {
        if (stockFood == null) {
            throw new BusinessException("A comida com ID " + foodId + " não está no estoque.");
        }
    }

    public void throwIfLoggedEntrepreneurIsDifferentFromRouteId(Long id, SisconfUserDetails userDetails) {
        Entrepreneur entrepreneur = findEntrepreneurById(id);
        personService.throwIfLoggedPersonIsDifferentFromPersonResource(userDetails.getKeycloakId(), entrepreneur.getPerson());
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

    public StockResponseDTO getByEntrepreneurId(Long entrepreneurId, SisconfUserDetails userDetails) {
        Entrepreneur entrepreneur = findEntrepreneurById(entrepreneurId);
        personService.throwIfLoggedPersonIsDifferentFromPersonResource(userDetails.getKeycloakId(), entrepreneur.getPerson());
        Stock stock = entrepreneur.getStock();
        return stockMapper.toResponseDTO(stock);
    }

    public StockResponseDTO associateFoods(Long entrepreneurId, StockFoodRequestDTO stockFoodRequestDTO, SisconfUserDetails userDetails) {
        throwIfLoggedEntrepreneurIsDifferentFromRouteId(entrepreneurId, userDetails);

        Stock stock = findStock(entrepreneurId);
        List<Long> foodsIds = getFoodsIds(stockFoodRequestDTO);
        List<Food> foundFoods = foodRepository.findAllById(foodsIds);
        Map<Long, Food> foodMap = createFoodMap(foundFoods);

        throwIfNotEveryFoodIsFound(foodsIds, foundFoods);

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

    public void updateStockFoodQuantity(Long entrepreneurId, StockFoodRequestDTO stockFoodRequestDTO, SisconfUserDetails userDetails) {
        throwIfLoggedEntrepreneurIsDifferentFromRouteId(entrepreneurId, userDetails);
        
        Stock stock = findStock(entrepreneurId);
        List<Long> foodsIds = getFoodsIds(stockFoodRequestDTO);
        List<Food> foundFoods = foodRepository.findAllById(foodsIds);

        throwIfNotEveryFoodIsFound(foodsIds, foundFoods);

        List<StockFood> stockFoods = stockFoodRepository.findByStockIdAndFoodIdIn(stock.getId(), foodsIds);
        Map<Long, StockFood> stockFoodMap = createStockFoodMap(stockFoods);

        List<StockFood> stockFoodsToBeUpdated = new ArrayList<>();
        for (StockFoodListRequestDTO foodItem : stockFoodRequestDTO.getFoods()) {
            StockFood stockFood = stockFoodMap.get(foodItem.getFoodId());

            throwIfStockFoodIsNotFoundInStock(stockFood, foodItem.getFoodId());

            stockFood.setQuantity(foodItem.getQuantity());
            stockFoodsToBeUpdated.add(stockFood);
        }

        stockFoodRepository.saveAll(stockFoodsToBeUpdated);
    }

    public void removeFoodsFromStock(Long entrepreneurId, StockFoodDeleteRequestDTO stockFoodDeleteRequestDTO, SisconfUserDetails userDetails) {
        throwIfLoggedEntrepreneurIsDifferentFromRouteId(entrepreneurId, userDetails);
        
        Stock stock = findStock(entrepreneurId);
        List<Long> foodsIds = stockFoodDeleteRequestDTO.getFoodsIds();
        List<Food> foundFoods = foodRepository.findAllById(foodsIds);

        throwIfNotEveryFoodIsFound(foodsIds, foundFoods);

        List<StockFood> stockFoods = stockFoodRepository.findByStockIdAndFoodIdIn(stock.getId(), foodsIds);
        Map<Long, StockFood> stockFoodMap = createStockFoodMap(stockFoods);

        List<StockFood> stockFoodsToBeDeleted = new ArrayList<>();
        for (Long foodId : stockFoodDeleteRequestDTO.getFoodsIds()) {
            StockFood stockFood = stockFoodMap.get(foodId);

            throwIfStockFoodIsNotFoundInStock(stockFood, foodId);

            stockFoodsToBeDeleted.add(stockFood);
        }

        stockFoodRepository.deleteAll(stockFoodsToBeDeleted);
    }
}
