package com.valoreterritoriale.digitalizzazioneterritoriale.service;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.UtenteRepository;
import org.springframework.stereotype.Service;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;

    // Constructor injection
    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public void creaUtente(Utente utente) {
        utenteRepository.save(utente);
    }

    public Utente modificaRuoloUtente(Integer id, String nuovoRuolo) {
        Utente existingUtente = utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        existingUtente.setRuolo(nuovoRuolo);
        return utenteRepository.save(existingUtente);
    }


    public boolean cancellaUtente(Integer id) {
        if (utenteRepository.existsById(id)) {
            utenteRepository.deleteById(id);
            return true;  // Utente esiste ed è stato cancellato
        } else {
            return false; // Utente non esiste
        }
    }

    public Utente visualizzaUtente(Integer id) {
        return utenteRepository.findById(id).orElse(null);
    }

}
