package org.generation.italy.demo.pojo;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table
public class Drink {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@NotEmpty(message = "il nome deve contenere qualcosa")
	@Column(unique = true)
	private String nome;
	
	@Lob
	@Nullable
	private String descrizione;
	
	@NotNull(message = "il prezzo non può essere vuoto")
	@Min(value=1, message = "il prezzo deve essere maggiore di zero")
	private int prezzo;
	
	public Drink() { }
	public Drink(String nome, String descrizione, int prezzo) {
		
		setId(id);
		setNome(nome);
		setDescrizione(descrizione);
		setPrezzo(prezzo);
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
	
	@Override
	public String toString() {
		
		return getId() + " - " + getNome()
			+ "\nDescrizione: " + getDescrizione()
			+ "\nPrezzo: " + getPrezzo();
	}
}
