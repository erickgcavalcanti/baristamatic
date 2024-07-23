package com.example.baristamatic.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.baristamatic.model.Drink;

@Repository
public interface DrinkRepository extends JpaRepository<Drink, Long>{
	
	Optional<Drink> findByName(String name);
	
	@Query("SELECT d FROM Drink d WHERE d.name = :name AND d.id != :idDrink")
	List<Drink> findByNameWithDifferentId(@Param("name") String name, @Param("idDrink") Long idDrink);

}
