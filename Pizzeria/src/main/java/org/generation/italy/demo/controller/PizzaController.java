package org.generation.italy.demo.controller;

import java.util.List;
import java.util.Optional;

import org.generation.italy.demo.pojo.Ingredient;
import org.generation.italy.demo.pojo.Pizza;
import org.generation.italy.demo.pojo.Promotion;
import org.generation.italy.demo.service.IngredientService;
import org.generation.italy.demo.service.PizzaService;
import org.generation.italy.demo.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/pizza")
public class PizzaController {

	@Autowired
	private PizzaService pizzaService;
	
	@Autowired
	private PromotionService promotionService;
	
	@Autowired
	private IngredientService ingredientService;
	
	@GetMapping("/user")
	public String getPizzas(Model model) {
		
		List<Pizza> pizzas = pizzaService.findAll();
		model.addAttribute("pizzas", pizzas);
		
		return "pizzas";
	}
	
	@GetMapping("/user/{id}")
	public String getPizza(@PathVariable("id") int id, Model model) {
		
		Optional<Pizza> optPizza = pizzaService.findPizzaById(id);
		
		if(optPizza.isEmpty()) {
			System.err.println("Pizza non presente con id: " + id);
		}
		
		Pizza pizza = optPizza.get();
		
		model.addAttribute("pizza", pizza);
		
		return "pizza";
	}
	
	@GetMapping("/admin/create")
	public String createPizza(Model model) {
		
		List<Ingredient> ingredients = ingredientService.findAllWPizza();
		List<Promotion> promotions = promotionService.findAll();
		Pizza pizza = new Pizza();
		model.addAttribute("pizza", pizza);
		model.addAttribute("promotions", promotions);
		model.addAttribute("ingredients", ingredients);
		
		return "pizza-create";
	}
	
	@PostMapping("/admin/create")
	public String storePizza(@Valid @ModelAttribute("pizza") Pizza pizza, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		if(bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			return "redirect:/pizza/create";
		}
		
		pizzaService.save(pizza);
		
		return "redirect:/pizza/user";
	}
	
	@GetMapping("/admin/update/{id}")
	public String editPizza(@PathVariable("id") int id, Model model) {
		
		Optional<Pizza> optPizza = pizzaService.findPizzaById(id);
		Pizza pizza = optPizza.get();
		model.addAttribute("pizza", pizza);
		
		List<Promotion> promotions = promotionService.findAll();
		model.addAttribute("promotions", promotions);
		
		List<Ingredient> ingredients = ingredientService.findAll();
		model.addAttribute("ingredients", ingredients);
		
		return "pizza-update";
	}
	
	@PostMapping("/admin/store")
	public String updatePizza(@Valid @ModelAttribute("pizza") Pizza pizza, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		
		if(bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
			return "redirect:/pizza/update/" + pizza.getId();
		}
		
		pizzaService.save(pizza);		
		
		return "redirect:/pizza/user";
	}
	
	@GetMapping("/admin/delete/{id}")
	public String deletePizza(@PathVariable("id") int id) {
		
		Optional<Pizza> optPizza = pizzaService.findPizzaById(id);
		Pizza pizza = optPizza.get();
		
		pizzaService.delete(pizza);
		
		return "redirect:/pizza/user";
	}
	
	@GetMapping("/user/search")
	public String getSearchPizzaByName(Model model, 
			@RequestParam(name = "q", required = false) String query) {

//		List<Pizza> pizzas = null;
//		if (query == null) {
//			
//			pizzas = pizzaService.findAll();
//			
//		} else {
//			
//			pizzas = pizzaService.findByName(query);
//		}
		
		List<Pizza> pizzas = query == null 
							? pizzaService.findAll()
							: pizzaService.findByNome(query); 

		model.addAttribute("pizzas", pizzas);
		model.addAttribute("query", query);

		return "pizzas-search";
	}
}
