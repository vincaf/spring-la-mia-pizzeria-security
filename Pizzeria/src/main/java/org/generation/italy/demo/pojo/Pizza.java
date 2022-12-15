package org.generation.italy.demo.pojo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table
public class Pizza {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@NotEmpty(message = "il nome deve contenere qualcosa")
	@Column(unique = true)
	private String nome;
	
	@NotNull
	private String descrizione;
	
	@NotNull(message = "il prezzo non può essere vuoto")
	@Min(value=1, message = "il prezzo deve essere maggiore di zero")
	private int prezzo;
	
	@ManyToOne
	@JoinColumn(name="promotion_id", nullable=true)
	private Promotion promotion;
	
	@ManyToMany
	private List<Ingredient> ingredients;
	
	public Pizza() { }
	public Pizza(String nome, String descrizione, int prezzo, Promotion promotion) {
		
		setId(id);
		setNome(nome);
		setDescrizione(descrizione);
		setPrezzo(prezzo);
		setPromotion(promotion);
	}
	public Pizza(String nome, String descrizione, int prezzo, Promotion promotion, List<Ingredient> ingredients) {
		this(nome, descrizione, prezzo, promotion);
		setIngredients(ingredients);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public int getPrezzo() {
		return prezzo;
	}
	public void setPrezzo(int prezzo) {
		this.prezzo = prezzo;
	}
	public Promotion getPromotion() {
		return promotion;
	}
	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}
	public List<Ingredient> getIngredients() {
		return ingredients;
	}
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
	public void addIngredients(Ingredient ingredient) {
		
		boolean finded = false;
		for (Ingredient i : getIngredients()) {
			
			if(i.getId() == ingredient.getId())
				finded = true;
		}
		
		if(!finded)
			getIngredients().add(ingredient);
	}
	
	public void removeIngredients(Ingredient ingredient) {
		getIngredients().remove(ingredient);
	}
	
	@Override
	public String toString() {
		
		return getId() + " - " + getNome()
			+ "\nDescrizione: " + getDescrizione()
			+ "\nPrezzo: " + getPrezzo();
	}
}
