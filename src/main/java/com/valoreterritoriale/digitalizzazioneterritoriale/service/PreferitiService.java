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

@Service
public class PreferitiService {
    private final PreferitiRepository preferitiRepository;
    private final ItinerarioRepository itinerarioRepository;
    private final UtenteRepository utenteRepository;

    public PreferitiService(PreferitiRepository preferitiRepository, ItinerarioRepository itinerarioRepository, UtenteRepository utenteRepository) {
        this.preferitiRepository = preferitiRepository;
        this.itinerarioRepository = itinerarioRepository;
        this.utenteRepository = utenteRepository;
    }

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

    public List<Preferito> visualizzaPreferiti() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        return preferitiRepository.findByUtente(utente);
    }


}