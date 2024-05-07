package com.valoreterritoriale.digitalizzazioneterritoriale.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

/**
 * Classe di configurazione della sicurezza per Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura la catena di filtri di sicurezza per gestire l'autenticazione e le autorizzazioni.
     *
     * @param http Oggetto HttpSecurity da configurare.
     * @return SecurityFilterChain configurato.
     * @throws Exception se si verifica un errore nella configurazione.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Configurazione delle regole di autorizzazione per le richieste HTTP.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/punti-di-interesse/visualizza/**","/punti-di-interesse/osm-info/**", "/itinerario/visualizza/**","/utente/crea", "/h2-console/**").permitAll()  // Permette l'accesso pubblico alle pagine specificate.
                        .requestMatchers("/utente/crea", "/utente/visualizza/**", "/utente/cancella/**", "/utente/modifica-ruolo/**").hasRole("GESTOREPIATTAFORMA")  // Accesso limitato ai gestori della piattaforma.
                        .requestMatchers("/utente/richiediModificaRuolo").hasRole("CONTRIBUTORE")  // Accesso limitato ai contributori.
                        .requestMatchers("/punti-di-interesse/crea", "/itinerario/crea","/itinerario/aggiungiPoi").hasAnyRole("CURATORE", "CONTRIBUTOREAUTORIZZATO", "CONTRIBUTORE") // Accesso limitato a ruoli specificati per la creazione di contenuti.
                        .requestMatchers("/itinerario/aggiungiPoi", "/punti-di-interesse/approve/**", "/itinerario/approve/**", "/punti-di-interesse/pending", "/itinerario/da-approvare").hasRole("CURATORE") // Accesso limitato ai curatori per la gestione e approvazione.
                        .requestMatchers("/itinerario/cancella/**", "/punti-di-interesse/cancella/**").hasAnyRole("CURATORE", "CONTRIBUTOREAUTORIZZATO") // Accesso limitato per la cancellazione di contenuti.
                        .requestMatchers("/preferiti/**").hasRole("TURISTAAUTENTICATO")  // Accesso limitato ai turisti autenticati.
                        .requestMatchers("/contest-di-contribuzione/mostra/{id}").hasAnyRole("TURISTAAUTENTICATO", "CONTRIBUTORE", "CONTRIBUTOREAUTORIZZATO", "ANIMATORE") // Accesso a contest specifici per ruoli definiti.
                        .requestMatchers("/contest-di-contribuzione/partecipaAlContest").hasAnyRole("TURISTAAUTENTICATO", "CONTRIBUTORE", "CONTRIBUTOREAUTORIZZATO") // Permette la partecipazione a contest.
                        .requestMatchers("/contest-di-contribuzione/**").hasRole("ANIMATORE")
                        .anyRequest().authenticated() // Tutte le altre richieste richiedono autenticazione.
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Pagina di login personalizzata.
                        .loginProcessingUrl("/login")  // URL per il processing del login.
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            request.getSession().setAttribute("loginMessage", "Login effettuato con successo!");
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            request.getSession().setAttribute("loginErrorMessage", "Credenziali non valide.");
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL per il logout.
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            request.setAttribute("logoutMessage", "Logout effettuato con successo!");
                        })
                        .logoutSuccessUrl("/login?logout")  // Reindirizza dopo il logout.
                        .invalidateHttpSession(true)  // Invalida la sessione.
                        .clearAuthentication(true)  // Pulisce l'autenticazione.
                        .permitAll()
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            request.setAttribute("accessDeniedMessage", "Utente non autenticato. Accesso negato.");
                        })
                )
                .csrf(AbstractHttpConfigurer::disable)  // Disabilitazione della protezione CSRF.
                .headers(headers -> headers
                        .addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy", "frame-ancestors 'self'"))  // Imposta la politica di sicurezza dei contenuti.
                )
                .cors(AbstractHttpConfigurer::disable);  // Disabilitazione di CORS.

        return http.build();  // Costruisce e restituisce la configurazione di HttpSecurity.
    }

    /**
     * Bean per la codifica delle password con BCrypt.
     *
     * @return PasswordEncoder codificatore di password.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
