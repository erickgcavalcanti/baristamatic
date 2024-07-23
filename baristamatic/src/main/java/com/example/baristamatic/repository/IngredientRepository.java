package com.example.baristamatic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.baristamatic.model.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long>{
	
	Optional<Ingredient> findByName(String name);
	
	@Query("SELECT i FROM Ingredient i WHERE i.name = :name AND i.id != :idIngredient")
	List<Ingredient> findByNameWithDifferentId(@Param("name") String name, @Param("idIngredient") Long idIngredient);

}
