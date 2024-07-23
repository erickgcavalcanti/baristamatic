package com.example.baristamatic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.baristamatic.model.CoffeeOrder;

@Repository
public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long>{
	
}