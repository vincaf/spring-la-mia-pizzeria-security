### Repo
**spring-la-mia-pizzeria-security**

### Todo
#### Parte 1: ecosistema db
Come primo esercizio, replicare un semplice `Hello World - Security` implementando il minimo indispensabile per testare la login su rotte protette con diversi ruoli per gli utenti (come visto a lezione).

Come prima cosa aggiungere la *dipendenza* nel `pom`:
```xml 
		<dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-security</artifactId>
		</dependency>
```

Sara' necessario creare l'ecosistema minimo indispensabile per salvare le tabelle `User` e `Role` all'interno del db (*pojo* + *repo* + *service*) con relazione *Molti a Molti* da `User` a `Role`:

##### User
- username : String : not null : unique
- password : String : not null

##### Role
- name : String : not null : unique

##### Note:
###### User Repo:
Deve implementare la funzione per il recuper dello user a partire dallo `username`:
```java
Optional<User> findByUsername(String username);
```

###### User Service
Deve implementare `UserDetailsService` e il metodo `loadUserByUsername`:
```java
@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> userOpt = userRepo.findByUsername(username);
		
		if (userOpt.isEmpty()) throw new UsernameNotFoundException("User not found");
		
		return new DatabaseUserDetails(userOpt.get());
	}
```

#### Parte 2: Security
All'interno dello stesso progetto, aggiungere le due classi per la `Security`: la classe `DatabaseUserDetails` per la mappatura tra il *pojo* e i dettagli per la *login* e la classe `SecurityConf` per la configurazione:

##### `DatabaseUserDetails`
```java
public class DatabaseUserDetails implements UserDetails {

	private static final long serialVersionUID = 3731354320731070576L;
	
	private final User user;
	
	public DatabaseUserDetails(User user) {

		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Set<Role> roles = user.getRoles();
		Set<GrantedAuthority> grantRole = new HashSet<>();
		
		for (Role role : roles) 
			grantRole.add(new SimpleGrantedAuthority(role.getName()));
		
		return grantRole;
	}

	@Override
	public String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {

		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}	
}
```

##### `SecurityConf`
```java
@Configuration
public class SecurityConf {

	@Bean
	public SecurityFilterChain getFilterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests()
				.requestMatchers(HttpMethod.GET, "/user", "/user/**").hasAuthority("USER")
				.requestMatchers(HttpMethod.POST, "/user", "/user/**").hasAuthority("ADMIN")
				.requestMatchers("/admin", "/admin/**").hasAuthority("ADMIN")
				.requestMatchers("/useradmin", "/useradmin/**").hasAnyAuthority("USER", "ADMIN")	
				.requestMatchers("/**").permitAll()
			.and().formLogin()
			.and().logout()
		;

		return http.build();
	}
	
	@Bean
	public UserDetailsService getuseDetailsService() {
		
		return new UserServ();
	}
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider getAuthProvider() {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		
		provider.setUserDetailsService(getuseDetailsService());
		provider.setPasswordEncoder(getPasswordEncoder());
		
		return provider;
	}
}	
```
#### Parte 3: controller + testing
All'interno di un *Controller* creare delle rotte compatibili con i settaggi fatti al *punto 2* e verificare il funzionameno di *login* e *logout*
#### Parte 4: security in pizza
Introdurre il concetto di login all'interno del progetto pizza, in particolare creando 2 *gruppi di utenti* (`USER` e `ADMIN`) e impostare la sicurezza in modo da proteggere tutte le rotte in scrittura, rendendoli accessibili ai soli `ADMIN`. La *index* sara' accessibile a tutti, mentre la *show* solo agli `USER`.

**N.B.**: gli utenti saranno tutti predefiniti nel db attraverso il solito `CommandLineRunner`:
```java
	@Override
	public void run(String... args) throws Exception {
		
		Role userRole = new Role("USER");
		Role adminRole = new Role("ADMIN");
		
		roleServ.save(userRole);
		roleServ.save(adminRole);
		
		User userUser = new User("user", "{noop}userpws", userRole);
		User adminUser = new User("admin", "{noop}adminpws", adminRole);
		
		Set<Role> userAdminRoles = new HashSet<>();
		userAdminRoles.add(userRole);
		userAdminRoles.add(adminRole);
		User userAdminUser = new User("useradmin", "{noop}useradminpws", userAdminRoles);
		
		userServ.save(userUser);
		userServ.save(adminUser);
		userServ.save(userAdminUser);
	}
```
