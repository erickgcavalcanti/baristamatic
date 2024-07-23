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

import com.example.baristamatic.model.Drink;
import com.example.baristamatic.repository.DrinkRepository;
import com.example.baristamatic.service.DrinkService;
import com.example.baristamatic.util.ControllerMapping;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = ControllerMapping.DRINK_PATH)
public class DrinkController {
	
	
	@Autowired
	private DrinkRepository drinkRepository;
	
	@Autowired
	private DrinkService drinkService;
	
	@GetMapping
	public List<Drink> findAll(){
		return drinkService.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Drink> findById(@PathVariable Long id){
		Optional<Drink> drinkOptional = drinkRepository.findById(id);
		return drinkOptional.isPresent() ? ResponseEntity.ok(drinkOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Drink> insert(@Valid @RequestBody Drink drink, HttpServletResponse response){
		Drink drinkSaved = drinkService.insert(drink);
		if (drinkSaved != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(drinkSaved);
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(drinkSaved);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Drink> update(@PathVariable Long id, @Valid @RequestBody Drink drink){
		Drink drinkSaved = drinkService.update(id, drink);
		return ResponseEntity.ok(drinkSaved);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		drinkRepository.deleteById(id);
	}

}
