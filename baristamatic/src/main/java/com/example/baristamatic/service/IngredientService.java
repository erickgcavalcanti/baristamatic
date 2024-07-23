package com.example.baristamatic.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.baristamatic.model.Ingredient;
import com.example.baristamatic.repository.IngredientRepository;
import com.example.baristamatic.service.exception.DuplicateIngredientNameException;

@Service
public class IngredientService {

	@Autowired
	private IngredientRepository ingredientRepository;
	
	public List<Ingredient> findAll(){
		return ingredientRepository.findAll();
	}
	
	public Ingredient findIngredientById(Long id) {
		Ingredient ingredient = ingredientRepository.findById(id)
				.orElseThrow(() -> new EmptyResultDataAccessException(1));
		return ingredient;
	}
	
	public Ingredient insert(Ingredient ingredient) {
		Ingredient ingredientSaved = save(ingredient);
		return ingredientSaved;
	}
	
	public Ingredient update(Long id, Ingredient ingredient) {
		Ingredient ingredientSaved = findIngredientById(id);
		BeanUtils.copyProperties(ingredient, ingredientSaved, "id");
		save(ingredientSaved);
		return ingredientSaved;
	}
	
	private void validateDuplicateIngredientName(Ingredient ingredient) {
		if(ingredient.getId() != null) {
			List<Ingredient> ingredients = ingredientRepository.findByNameWithDifferentId(ingredient.getName(), ingredient.getId());
			if(ingredients != null && !ingredients.isEmpty()) {
				throw new DuplicateIngredientNameException();
			}
		}else {
			if(ingredientRepository.findByName(ingredient.getName()).isPresent()) {
				throw new DuplicateIngredientNameException();
			}
		}
	}

	private Ingredient save(Ingredient ingredient) {
		validateDuplicateIngredientName(ingredient);
		return ingredientRepository.save(ingredient);
	}
	
}
