package com.example.baristamatic.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.baristamatic.model.Drink;
import com.example.baristamatic.repository.DrinkRepository;
import com.example.baristamatic.service.exception.DuplicateDrinkNameException;

@Service
public class DrinkService {

	@Autowired
	private DrinkRepository drinkRepository;
	
	public List<Drink> findAll(){
		return drinkRepository.findAll();
	}
	
	public Drink findDrinkById(Long id) {
		Drink drink = drinkRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		return drink;
	}
	
	public Drink insert(Drink drink) {
		Drink drinkSaved = save(drink);
		return drinkSaved;
	}
	
	public Drink update(Long id, Drink drink) {
		Drink drinkSaved = findDrinkById(id);
		BeanUtils.copyProperties(drink, drinkSaved, "id");
		save(drinkSaved);
		return drinkSaved;
	}
	
	private void validateDuplicateDrinkName(Drink drink) {
		if(drink.getId() != null) {
			List<Drink> drinks = drinkRepository.findByNameWithDifferentId(drink.getName(), drink.getId());
			if(drinks != null && !drinks.isEmpty()) {
				throw new DuplicateDrinkNameException();
			}
		}else {
			if(drinkRepository.findByName(drink.getName()).isPresent()) {
				throw new DuplicateDrinkNameException();
			}
		}
	}

	private Drink save(Drink drink) {
		validateDuplicateDrinkName(drink);
		return drinkRepository.save(drink);
	}
	
}
