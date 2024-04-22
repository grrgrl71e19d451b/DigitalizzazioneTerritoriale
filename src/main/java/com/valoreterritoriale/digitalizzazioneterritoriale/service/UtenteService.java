package com.valoreterritoriale.digitalizzazioneterritoriale.service;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class UtenteService implements UserDetailsService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void creaUtente(Utente utente) {
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utenteRepository.save(utente);
    }

    public Utente modificaRuoloUtente(Long id, String nuovoRuolo) {  // Modificato il tipo da Integer a Long
        Utente existingUtente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        existingUtente.setRuolo(nuovoRuolo);
        return utenteRepository.save(existingUtente);
    }

    public boolean cancellaUtente(Long id) {  // Modificato il tipo da Integer a Long
        if (utenteRepository.existsById(id)) {
            utenteRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Utente visualizzaUtente(Long id) {  // Modificato il tipo da Integer a Long
        return utenteRepository.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));
        Set<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + utente.getRuolo()));
        return new User(utente.getUsername(), utente.getPassword(), authorities);
    }
}
