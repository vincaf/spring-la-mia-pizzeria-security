package org.generation.italy.demo.controller;

import java.util.List;

import org.generation.italy.demo.pojo.Drink;
import org.generation.italy.demo.pojo.Pizza;
import org.generation.italy.demo.service.DrinkService;
import org.generation.italy.demo.service.PizzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/available")
public class ManagerController {

	@Autowired
	private PizzaService pizzaServ;
	
	@Autowired
	private DrinkService drinkServ;
	
	@GetMapping
	public String getAllByName(Model model,
			@RequestParam(name="query", required=false) String query) {
		
		List<Pizza> pizzas = null;
		List<Drink> drinks = null;
		
		if(query == null || query.isEmpty()) {
			
			pizzas = pizzaServ.findAll();
			drinks = drinkServ.findAll();
		} else {
			
			pizzas = pizzaServ.findByNome(query);
			drinks = drinkServ.findByNome(query);
		}
		
		model.addAttribute("pizzas", pizzas);
		model.addAttribute("drinks", drinks);
		
		return "available-search";
	}
}
