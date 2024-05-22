package com.valoreterritoriale.digitalizzazioneterritoriale.service;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Itinerario;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Preferito;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.ItinerarioRepository;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.PreferitiRepository;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.UtenteRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servizio per la gestione degli itinerari preferiti degli utenti.
 */
@Service
public class PreferitiService {
    private final PreferitiRepository preferitiRepository;
    private final ItinerarioRepository itinerarioRepository;
    private final UtenteRepository utenteRepository;

    /**
     * Costruttore per iniezione delle dipendenze dei repository necessari.
     *
     * @param preferitiRepository Repository per la gestione dei preferiti.
     * @param itinerarioRepository Repository per la gestione degli itinerari.
     * @param utenteRepository Repository per la gestione degli utenti.
     */
    public PreferitiService(PreferitiRepository preferitiRepository, ItinerarioRepository itinerarioRepository, UtenteRepository utenteRepository) {
        this.preferitiRepository = preferitiRepository;
        this.itinerarioRepository = itinerarioRepository;
        this.utenteRepository = utenteRepository;
    }

    /**
     * Aggiunge un itinerario alla lista dei preferiti di un utente.
     *
     * @param itinerarioId Identificativo dell'itinerario da aggiungere ai preferiti.
     * @return true se l'itinerario è stato aggiunto con successo.
     * @throws IllegalArgumentException se l'utente o l'itinerario non sono trovati.
     */
    public boolean aggiungiItinerarioAiPreferiti(Long itinerarioId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        Itinerario itinerario = itinerarioRepository.findById(itinerarioId)
                .orElseThrow(() -> new IllegalArgumentException("Itinerario non trovato"));

        Preferito preferito = new Preferito(utente, itinerario);
        preferitiRepository.save(preferito);
        return true;
    }

    /**
     * Rimuove un itinerario dalla lista dei preferiti di un utente.
     *
     * @param itinerarioId Identificativo dell'itinerario da rimuovere dai preferiti.
     * @return true se l'itinerario è stato rimosso con successo, false se non è presente nella lista dei preferiti.
     * @throws IllegalArgumentException se l'utente o l'itinerario non sono trovati.
     */
    public boolean rimuoviItinerarioDaiPreferiti(Long itinerarioId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        Itinerario itinerario = itinerarioRepository.findById(itinerarioId)
                .orElseThrow(() -> new IllegalArgumentException("Itinerario non trovato"));

        Optional<Preferito> preferito = preferitiRepository.findByUtenteAndItinerario(utente, itinerario);
        if (preferito.isPresent()) {
            preferitiRepository.delete(preferito.get());
            return true;
        }
        return false;
    }

    /**
     * Restituisce la lista degli itinerari preferiti dell'utente autenticato.
     *
     * @return Lista dei preferiti dell'utente.
     * @throws IllegalArgumentException se l'utente non è trovato.
     */
    public List<Preferito> visualizzaPreferiti() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        return preferitiRepository.findByUtente(utente);
    }
}
