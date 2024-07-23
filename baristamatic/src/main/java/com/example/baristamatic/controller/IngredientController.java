package com.example.baristamatic.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.baristamatic.model.Ingredient;
import com.example.baristamatic.repository.IngredientRepository;
import com.example.baristamatic.service.IngredientService;
import com.example.baristamatic.util.ControllerMapping;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = ControllerMapping.INGREDIENT_PATH)
public class IngredientController {
	
	
	@Autowired
	private IngredientRepository ingredientRepository;
	
	@Autowired
	private IngredientService ingredientService;
	
	@GetMapping
	public List<Ingredient> findAll(){
		return ingredientService.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Ingredient> findById(@PathVariable Long id){
		Optional<Ingredient> ingredientOptional = ingredientRepository.findById(id);
		return ingredientOptional.isPresent() ? ResponseEntity.ok(ingredientOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Ingredient> insert(@Valid @RequestBody Ingredient ingredient, HttpServletResponse response){
		Ingredient ingredientSaved = ingredientService.insert(ingredient);
		if (ingredientSaved != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(ingredientSaved);
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ingredientSaved);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Ingredient> update(@PathVariable Long id, @Valid @RequestBody Ingredient ingredient){
		Ingredient ingredientSaved = ingredientService.update(id, ingredient);
		return ResponseEntity.ok(ingredientSaved);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		ingredientRepository.deleteById(id);
	}

}
