package com.example.baristamatic.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "coffee_order_item")
public class CoffeeOrderItem {
	
	private Long id;
	private CoffeeOrder coffeeOrder;
	private Drink drink;
	private Long quantity;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "idCoffeeOrder")
	@JsonBackReference
	public CoffeeOrder getCoffeeOrder() {
		return coffeeOrder;
	}
	
	public void setCoffeeOrder(CoffeeOrder coffeeOrder) {
		this.coffeeOrder = coffeeOrder;
	}
	
	@NotNull
	@ManyToOne(optional = false)
	@JoinColumn(name = "idDrink")
	public Drink getDrink() {
		return drink;
	}

	public void setDrink(Drink drink) {
		this.drink = drink;
	}
	
	@Min(value=0)
	public Long getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

}
