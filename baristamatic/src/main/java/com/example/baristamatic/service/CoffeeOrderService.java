package com.example.baristamatic.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.baristamatic.model.CoffeeOrder;
import com.example.baristamatic.model.CoffeeOrderItem;
import com.example.baristamatic.model.Drink;
import com.example.baristamatic.model.Ingredient;
import com.example.baristamatic.model.IngredientItem;
import com.example.baristamatic.repository.CoffeeOrderRepository;
import com.example.baristamatic.service.exception.InsufficientIngredientsException;

@Service
public class CoffeeOrderService {

	@Autowired
	private CoffeeOrderRepository coffeeOrderRepository;
	
	@Autowired
	private DrinkService drinkService;
	
	@Autowired
	private IngredientService ingredientService;
	
	public List<CoffeeOrder> findAll(){
		return coffeeOrderRepository.findAll();
	}
	
	public Optional<CoffeeOrder> findCoffeeOrderById(Long id) {
		Optional<CoffeeOrder> coffeeOrder = coffeeOrderRepository.findById(id);
		return coffeeOrder;
	}
	
	public CoffeeOrder insert(CoffeeOrder coffeeOrder) {
		CoffeeOrder coffeeOrderSaved = save(coffeeOrder);
		return coffeeOrderSaved;
	}
	
	public CoffeeOrder update(Long id, CoffeeOrder coffeeOrder) {
		deleteById(id);
		save(coffeeOrder);
		return coffeeOrder;
	}

	@Transactional
	public void updateIngredients(CoffeeOrder coffeeOrder, boolean increment) {
		BigDecimal total = BigDecimal.ZERO;
		if (coffeeOrder != null && coffeeOrder.getCoffeeOrderItems() != null && !coffeeOrder.getCoffeeOrderItems().isEmpty()) {
			for (CoffeeOrderItem coffeeOrderItem : coffeeOrder.getCoffeeOrderItems()) {
				if (coffeeOrderItem != null && coffeeOrderItem.getDrink() != null) {
					
					Drink drink = drinkService.findDrinkById(coffeeOrderItem.getDrink().getId());
					total = total.add(drink.getCost().multiply(BigDecimal.valueOf(coffeeOrderItem.getQuantity())));
					if (drink.getIngredientItems() != null && !drink.getIngredientItems().isEmpty()) {

						for (IngredientItem ingredientItem : drink.getIngredientItems()) {

							if (ingredientItem != null && ingredientItem.getIngredient() != null) {
								Ingredient ingredient = ingredientService
										.findIngredientById(ingredientItem.getIngredient().getId());
								Long quantity = ingredientItem.getQuantity() * coffeeOrderItem.getQuantity();
								if (increment) {
									ingredient.setTotal(ingredient.getTotal() + quantity);
									ingredientService.update(ingredient.getId(), ingredient);
								} else {
									if (ingredient.getTotal() - quantity >= 0) {
										ingredient.setTotal(ingredient.getTotal() - quantity);
										ingredientService.update(ingredient.getId(), ingredient);
									} else {
										throw new InsufficientIngredientsException();
									}
								}
							}
						}
					}
				}
			}
		}
		coffeeOrder.setTotal(total);

	}
	
	@Transactional
	private CoffeeOrder save(CoffeeOrder coffeeOrder) {
		updateIngredients(coffeeOrder, false);
		return coffeeOrderRepository.save(coffeeOrder);
	}
	
	@Transactional
	public void deleteById(Long id) {
		Optional<CoffeeOrder> coffeeOrder = findCoffeeOrderById(id);
		if(coffeeOrder.isPresent()) {
			updateIngredients(coffeeOrder.get(), true);
		}
		coffeeOrderRepository.deleteById(id);
	}
	
}