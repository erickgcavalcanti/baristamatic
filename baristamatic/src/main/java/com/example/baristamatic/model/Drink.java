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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "drink")
public class Drink {

	private Long id;
	private String name;
	private List<IngredientItem> ingredientItems;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@NotNull
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "drink")
	@JsonManagedReference
	public List<IngredientItem> getIngredientItems() {
		return ingredientItems;
	}

	public void setIngredientItems(List<IngredientItem> ingredientItems) {
		this.ingredientItems = ingredientItems;
	}
	
	@Transient
	public BigDecimal getCost() {
		BigDecimal cost = BigDecimal.ZERO;
		if(ingredientItems != null && !ingredientItems.isEmpty()) {
			for(IngredientItem ingredientItem : ingredientItems) {
				cost = cost.add(ingredientItem.getIngredient().getUnitCost().multiply(BigDecimal.valueOf(ingredientItem.getQuantity())));
			}
		}
		return cost;
	}
	
	@Transient
	public Boolean isAvailability() {
		Boolean availability = false;
		if(ingredientItems != null && !ingredientItems.isEmpty()) {
			for(IngredientItem ingredientItem : ingredientItems) {
				if(ingredientItem.getIngredient().getTotal() < ingredientItem.getQuantity()) {
					return false;
				}
			}
			availability = true;
		}
		return availability;
	}

//	@ManyToMany
//	@JoinTable(name = "drink_ingredient", joinColumns = @JoinColumn(name = "idDrink"), inverseJoinColumns = @JoinColumn(name = "idIngredient"))
//	public List<Ingredient> getIngredients() {
//		return ingredients;
//	}
//
//	public void setIngredients(List<Ingredient> ingredients) {
//		this.ingredients = ingredients;
//	}
//	
}