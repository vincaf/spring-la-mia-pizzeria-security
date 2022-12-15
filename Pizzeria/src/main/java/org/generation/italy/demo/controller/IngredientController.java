package org.generation.italy.demo.controller;

import java.util.List;

import org.generation.italy.demo.pojo.Ingredient;
import org.generation.italy.demo.pojo.Pizza;
import org.generation.italy.demo.service.IngredientService;
import org.generation.italy.demo.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/ingredient")
public class IngredientController {

	@Autowired
	private IngredientService ingredientService;

	@Autowired
	private PizzaService pizzaService;

	@GetMapping
	public String index(Model model) {

		List<Ingredient> ingredients = ingredientService.findAllWPizza();
		model.addAttribute("ingredients", ingredients);
		return "ingredients";
	}

	@GetMapping("/create")
	public String getIngredientCreate(Model model) {

		Ingredient ingredient = new Ingredient();
		model.addAttribute("ingredient", ingredient);
		
		List<Pizza> pizzas = pizzaService.findAll();
		model.addAttribute("pizzas", pizzas);
		
		return "ingredient-create";
	}
	
	@PostMapping("/create")
	public String storeIngredient(@Valid Ingredient ingredient) {

//		List<Pizza> ingredientPizzas = ingredient.getPizzas();
//		for (Pizza pizza : ingredientPizzas)
//			pizza.getIngredients().add(ingredient);
		
		for (Pizza p : ingredient.getPizzas()) {
			
			p.getIngredients().add(ingredient);
		}
		
		ingredientService.save(ingredient);
		
		return "redirect:/ingredient";
	}
	
	@GetMapping("/update/{id}")
	public String updateIngredient(@PathVariable("id") int id, Model model) {
		
		Ingredient ingredient = ingredientService.getIngredientById(id);
		model.addAttribute("ingredient", ingredient);
		
		List<Pizza> pizzas = pizzaService.findAll();
		model.addAttribute("pizzas", pizzas);
		
		return "ingredient-update";
	}
	
	@PostMapping("/update/{id}")
	public String editIngredient(@PathVariable("id") int id, @Valid Ingredient ingredient) {
		
		Ingredient oldIng = ingredientService.getIngredientById(id);
		
		for (Pizza pizza : oldIng.getPizzas()) {
			
			pizza.getIngredients().remove(oldIng);
		}
		
		for (Pizza pizza : ingredient.getPizzas()) {
			
			pizza.addIngredients(ingredient);
		}
		
		ingredientService.save(ingredient);
		
		return "redirect:/ingredient";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteIngredient(@PathVariable("id") int id) {

		Ingredient i = ingredientService.getIngredientById(id);
		for (Pizza pizza : i.getPizzas())
			pizza.removeIngredients(i);
		
		ingredientService.deleteIngredientById(id);
		return "redirect:/ingredient";
	}
}
