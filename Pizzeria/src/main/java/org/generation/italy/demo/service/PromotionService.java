package org.generation.italy.demo.service;

import java.util.List;
import java.util.Optional;

import org.generation.italy.demo.pojo.Promotion;
import org.generation.italy.demo.repo.PromotionRepo;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class PromotionService {

	@Autowired
	private PromotionRepo promotionRepo;

	public void save(Promotion promotion) {
		promotionRepo.save(promotion);
	}

	public List<Promotion> findAll() {
		return promotionRepo.findAll();
	}

	public Optional<Promotion> getPromotionById(int id) {
		return promotionRepo.findById(id);
	}

	public void deletePromotionById(int id) {
		promotionRepo.deleteById(id);
	}
	
	@Transactional
	public List<Promotion> findAllWPizza() {
		List<Promotion> promotions = promotionRepo.findAll();

		for (Promotion promotion : promotions) {
			Hibernate.initialize(promotion.getPizzas());
		}

		return promotions;
	}
}