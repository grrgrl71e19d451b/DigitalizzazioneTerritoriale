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

/**
 * Servizio per la gestione degli utenti nel contesto della sicurezza di Spring.
 */
@Service
public class UtenteService implements UserDetailsService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Costruttore con iniezione delle dipendenze necessarie per il repository degli utenti e l'encoder per le password.
     *
     * @param utenteRepository il repository per la gestione degli utenti
     * @param passwordEncoder il codificatore di password utilizzato per hashare le password degli utenti
     */
    @Autowired
    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Crea un nuovo utente codificando la sua password prima di salvarlo nel database.
     *
     * @param utente l'entità utente da salvare
     */
    public void creaUtente(Utente utente) {
        utente.setPassword(passwordEncoder.encode(utente.getPassword()));
        utenteRepository.save(utente);
    }

    /**
     * Modifica il ruolo di un utente esistente.
     *
     * @param id l'identificativo dell'utente da modificare
     * @param nuovoRuolo il nuovo ruolo da assegnare all'utente
     * @return l'utente aggiornato
     */
    public Utente modificaRuoloUtente(Long id, String nuovoRuolo) {
        Utente existingUtente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        existingUtente.setRuolo(nuovoRuolo);
        return utenteRepository.save(existingUtente);
    }

    /**
     * Cancella un utente dal database.
     *
     * @param id l'identificativo dell'utente da cancellare
     * @return true se l'utente è stato cancellato con successo, false altrimenti
     */
    public boolean cancellaUtente(Long id) {
        if (utenteRepository.existsById(id)) {
            utenteRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Visualizza un utente tramite il suo ID.
     *
     * @param id l'ID dell'utente da visualizzare
     * @return l'utente trovato o null se non esiste
     */
    public Utente visualizzaUtente(Long id) {
        return utenteRepository.findById(id).orElse(null);
    }

    /**
     * Carica un utente tramite il suo nome utente per l'autenticazione.
     *
     * @param username il nome utente dell'utente da caricare
     * @return i dettagli dell'utente necessari per l'autenticazione
     * @throws UsernameNotFoundException se l'utente non viene trovato
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + utente.getRuolo());
        return new User(utente.getUsername(), utente.getPassword(), Collections.singleton(authority));
    }
}
