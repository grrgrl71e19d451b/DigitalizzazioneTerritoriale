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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Configurazione delle autorizzazioni delle richieste HTTP
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/punti-di-interesse/visualizza/**", "/itinerario/visualizza/**", "/h2-console/**").permitAll()  // Accesso pubblico a visualizzare punti di interesse e itinerari
                        .requestMatchers("/utente/crea").hasAnyRole("GESTOREPIATTAFORMA", "TURISTA") // Solo gestore della piattaforma e turisti possono creare utenti
                        .requestMatchers("/utente/visualizza/**", "/utente/cancella/**", "/utente/modifica-ruolo/**").hasRole("GESTOREPIATTAFORMA") // Gestione degli utenti limitata al gestore della piattaforma
                        .requestMatchers("/punti-di-interesse/crea", "/itinerario/crea").hasAnyRole("CURATORE", "CONTRIBUTOREAUTORIZZATO") // Creazione di punti di interesse e itinerari limitata a curatori e contributori autorizzati
                        .requestMatchers("/itinerario/aggiungiPoi", "/punti-di-interesse/approve/**", "/itinerario/approve/**", "/punti-di-interesse/pending", "/itinerario/da-approvare").hasRole("CURATORE") // Gestione e approvazione di punti di interesse e itinerari limitata ai curatori
                        .requestMatchers("/itinerario/cancella/**", "/punti-di-interesse/cancella/**").hasAnyRole("CURATORE", "CONTRIBUTOREAUTORIZZATO") // Cancellazione limitata a curatori e contributori autorizzati
                        .anyRequest().authenticated() // Tutte le altre richieste richiedono autenticazione
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
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
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                            request.setAttribute("logoutMessage", "Logout effettuato con successo!");
                        })
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            request.setAttribute("accessDeniedMessage", "Utente non autenticato. Accesso negato.");
                        })
                )
                .csrf(AbstractHttpConfigurer::disable)  // Disabilita completamente la protezione CSRF
                .headers(headers -> headers
                        .addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy", "frame-ancestors 'self'"))  // Imposta una politica di sicurezza del contenuto per prevenire clickjacking
                )
                .cors(AbstractHttpConfigurer::disable);  // Disabilita CORS

        return http.build();  // Costruisce la configurazione HttpSecurity
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
