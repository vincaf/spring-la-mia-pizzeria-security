package org.generation.italy.demo.service;

import java.util.List;
import java.util.Optional;

import org.generation.italy.demo.pojo.Ingredient;
import org.generation.italy.demo.repo.IngredientRepo;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class IngredientService {

	@Autowired
	private IngredientRepo ingredientRepo;

	public void save(Ingredient i) {
		ingredientRepo.save(i);
	}

	public List<Ingredient> findAll() {
		return ingredientRepo.findAll();
	}

	public Ingredient getIngredientById(int id) {
		return ingredientRepo.findById(id).get();
	}

	public void deleteIngredientById(int id) {
		ingredientRepo.deleteById(id);
	}

	@Transactional
	public List<Ingredient> findAllWPizza() {
		List<Ingredient> ingredients = ingredientRepo.findAll();

		for (Ingredient i : ingredients) {
			Hibernate.initialize(i.getPizzas());
		}

		return ingredients;
	}
}