### Repo
**spring-la-mia-pizzeria-relazioni**

### Todo
#### Parte 1
All'interno del progetto precedente, generare una nuova entità chiamata `Promozione` che sarà caratterizzata da:
- *dataInizio* : LocalDate : not null
- *dataFine* : LocalDate : not null
- *titolo* : string : not null : unique

Dopo aver testata l'entità pura (con relativi *repo* + *service*), creare la relazione tra `Pizza` e `Promozione` di tipo *1aN* (per ogni pizza esiste una sola promozione, ogni promozione puo' essere applicata a piu' pizze).
Dopo aver aggiustato i `pojo` con relative proprieta' e modifica del costruttore, all'interno dell'`Application` gestire la relazione testando la possibilita' di creare pizze con o senza promozione, e la possibilita' di eliminare sia le pizze, sia le promozioni (attenzione a come viene gestita la relazione in fase di cancellazione delle promozioni).

#### N.B.:
Attenzione ai nuovi metodi dei `Service`, che dovranno gestire se/quando la relazione verra' caricata all'interno della lista (vedere metodo `@Transactional`)

---

### **BONUS**
Creare il front-end per la index delle due relazioni, aggiungere un collegamento al form per creare nuovi elementi come nell'esercizio della settimana precedente (solo `Create`), aggiungendo il concetto di relazione tra `Pizza` e `Promozione` attraverso i componenti del `form` visti durante la live: 

##### HTML
**ManyToOne**
```html
	<form
		method="POST"
		action="/borrowing/store"
	>
		<label>Name:</label>
		<input type="text" name="name" th:field="*{name}">
		<br>
		<select name="book">
			<option
				th:each="book : ${books}"
				th:object="${book}"
				
				th:value="*{id}"
				th:field="${borrowing.book}"
			>
				[[*{name}]]
			</option>
		</select>
		<br><br>
		<input type="submit" value="CREATE NEW BORROWING">
	</form>
```

**OneToMany**
```html
	<form
		method="POST"
		action="/book/store"
	>
		<label>Name:</label>
		<input type="text" name="name" th:field="*{name}">
		<br>
		<div
			th:each="borrowing : ${borrowings}"
			th:object="${borrowing}"
		>
			<input 
				type="checkbox" 
				name="borrowings" 
				
				th:value="*{id}" 
				th:field="${book.borrowings}">
			<label>[[*{name}]]</label>
		</div>
		<br><br>
		<input type="submit" value="CREATE">
	</form>
```

**
##### JAVA
**OneToMany**
```java
	@PostMapping("/store")
	public String storeBook(
				@Valid Book book
			) {
		
		List<Borrowing> bookBorrowing = book.getBorrowings();
		for(Borrowing borrowing : bookBorrowing) {
			
			borrowing.setBook(book);
		}
		
		bookServ.save(book);
		
		return "redirect:/book";
	}
```

---

### Todo
#### Parte 2
Nell'esercizio precedente aggiungere l'entita' `Ingrediente` (con relativi *repo* + *service*). La nuova entita' sara' carraterizzata da:
- nome : String : not null

Testare la nuova entita' nel `run()` e poi aggiungere una relazione di tipo **ManyToMany** tra `Ingrediente` e `Pizza` (per ogni ingrediente esistono piu' pizze, per ogni pizza esistono piu' ingredienti). Testare all'interno del `run()` anche la relazione appena creata.

Aggiungere inoltre un controller dedicato alla nuova entita' `IngredienteController` che sara' in grado di mostrare attraverso le pagine `HTML` la lista di ingredienti con le relative pizze associate. Dovra' inoltre essere fornita all'utente la possibilita' di inserire nuovi ingredienti associati alle pizze e di creare nuove pizze associando gli ingredienti.

#### **Bonus**
Fornire la possibilita' di eliminare entita', e modificare le entita' presenti (sia `Ingrediente` che `Pizza`) valorizzando correttamente sia in lettura che in scrittura le relazioni.
