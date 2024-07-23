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

import com.example.baristamatic.model.CoffeeOrder;
import com.example.baristamatic.service.CoffeeOrderService;
import com.example.baristamatic.util.ControllerMapping;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = ControllerMapping.ORDER_PATH)
public class CoffeeOrderController {
	
	
	@Autowired
	private CoffeeOrderService coffeeOrderService;
	
	@GetMapping
	public List<CoffeeOrder> findAll(){
		return coffeeOrderService.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CoffeeOrder> findById(@PathVariable Long id){
		Optional<CoffeeOrder> coffeeOrderOptional = coffeeOrderService.findCoffeeOrderById(id);
		return coffeeOrderOptional.isPresent() ? ResponseEntity.ok(coffeeOrderOptional.get()) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<CoffeeOrder> insert(@Valid @RequestBody CoffeeOrder coffeeOrder, HttpServletResponse response){
		CoffeeOrder coffeeOrderSaved = coffeeOrderService.insert(coffeeOrder);
		if (coffeeOrderSaved != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(coffeeOrderSaved);
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(coffeeOrderSaved);
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CoffeeOrder> update(@PathVariable Long id, @Valid @RequestBody CoffeeOrder coffeeOrder){
		CoffeeOrder coffeeOrderSaved = coffeeOrderService.update(id, coffeeOrder);
		return ResponseEntity.ok(coffeeOrderSaved);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		coffeeOrderService.deleteById(id);
	}

}
