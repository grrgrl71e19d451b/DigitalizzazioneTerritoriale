package com.valoreterritoriale.digitalizzazioneterritoriale.service;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.ItinerarioCrea;
import com.valoreterritoriale.digitalizzazioneterritoriale.dto.PoiAItinerarioAggiungi;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Itinerario;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.PuntoDiInteresse;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.ItinerarioRepository;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.PuntoDiInteresseRepository;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ItinerarioService {

    private final ItinerarioRepository itinerarioRepository;
    private final PuntoDiInteresseRepository puntoDiInteresseRepository;
    private final UtenteRepository utenteRepository;  // Aggiunto campo per UtenteRepository

    @Autowired  // Costruttore modificato per iniettare anche UtenteRepository
    public ItinerarioService(ItinerarioRepository itinerarioRepository, PuntoDiInteresseRepository puntoDiInteresseRepository, UtenteRepository utenteRepository) {
        this.itinerarioRepository = itinerarioRepository;
        this.puntoDiInteresseRepository = puntoDiInteresseRepository;
        this.utenteRepository = utenteRepository;
    }


    @Transactional
    public Itinerario creaItinerario(ItinerarioCrea itinerarioDTO) {
        // Ottenere l'utente loggato dal contesto di sicurezza
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Assumendo che il nome utente sia unico

        // Trovare l'utente dal repository usando il nome utente
        Utente creatore = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Utente non trovato con username: " + username));

        // Creare l'itinerario usando l'utente loggato come creatore
        Itinerario itinerario = new Itinerario();
        itinerario.setCreatore(creatore);
        itinerario.setNomeItinerario(itinerarioDTO.getNomeItinerario());
        itinerario.setDescrizione(itinerarioDTO.getDescrizione());
        itinerario.setDataPubblicazione(itinerarioDTO.getDataPubblicazione());
        itinerario.setPending(itinerarioDTO.isPending());

        return itinerarioRepository.save(itinerario);
    }


    @Transactional
    public void aggiungiPoiAItinerario(PoiAItinerarioAggiungi dto) {
        Itinerario itinerario = itinerarioRepository.findById((long) dto.getIdItinerario())
                .orElseThrow(() -> new NoSuchElementException("Itinerario non trovato"));
        PuntoDiInteresse puntoDiInteresse = puntoDiInteresseRepository.findById((long) dto.getIdPuntoDiInteresse())
                .orElseThrow(() -> new NoSuchElementException("Punto di interesse non trovato"));

        itinerario.addPuntoDiInteresse(puntoDiInteresse);
        itinerarioRepository.save(itinerario);
    }

    @Transactional
    public boolean cancellaItinerario(Long id) {
        if (itinerarioRepository.existsById(id)) {
            itinerarioRepository.deleteById(id);
            return true; // Ritorna true se l'itinerario esiste ed è stato cancellato.
        } else {
            return false; // Ritorna false se l'itinerario non esiste.
        }
    }


    @Transactional
    public void approvaItinerario(Long id) {
        Itinerario itinerario = itinerarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Itinerario non trovato"));
        itinerario.setPending(false);
        itinerarioRepository.save(itinerario);
    }

    public List<Itinerario> visualizzaItinerariDaApprovare() {
        return itinerarioRepository.findByPendingTrue();
    }

    public Itinerario visualizzaItinerarioById(Long id) {
        return itinerarioRepository.findById(id).orElse(null);
    }
}