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

    // I tuoi metodi esistenti...

    @Transactional
    public Itinerario creaItinerario(ItinerarioCrea itinerarioDTO) {
        Utente creatore = utenteRepository.findById(itinerarioDTO.getIdUtente())
                .orElseThrow(() -> new NoSuchElementException("Utente non trovato"));
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
    public void cancellaItinerario(Long id) {
        Itinerario itinerario = itinerarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Itinerario non trovato"));
        itinerarioRepository.delete(itinerario);
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

    public Itinerario findItinerarioById(Long id) {
        return itinerarioRepository.findById(id).orElse(null);
    }
}