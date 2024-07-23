package com.example.baristamatic.model;

import java.math.BigDecimal;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "coffee_order")
public class CoffeeOrder {

	private Long id;
	private List<CoffeeOrderItem> coffeeOrderItems;
	private BigDecimal total;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "coffeeOrder")
	@JsonManagedReference
	public List<CoffeeOrderItem> getCoffeeOrderItems() {
		return coffeeOrderItems;
	}

	public void setCoffeeOrderItems(List<CoffeeOrderItem> coffeeOrderItems) {
		this.coffeeOrderItems = coffeeOrderItems;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}