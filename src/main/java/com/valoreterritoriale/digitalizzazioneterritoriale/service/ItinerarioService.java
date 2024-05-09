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

/**
 * Servizio per la gestione degli itinerari, inclusa la creazione e modifica.
 */
@Service
public class ItinerarioService {

    private final ItinerarioRepository itinerarioRepository;
    private final PuntoDiInteresseRepository puntoDiInteresseRepository;
    private final UtenteRepository utenteRepository;

    /**
     * Costruttore per iniezione delle dipendenze dei repository necessari.
     *
     * @param itinerarioRepository Repository per Itinerario.
     * @param puntoDiInteresseRepository Repository per Punto di Interesse.
     * @param utenteRepository Repository per Utente.
     */
    @Autowired
    public ItinerarioService(ItinerarioRepository itinerarioRepository, PuntoDiInteresseRepository puntoDiInteresseRepository, UtenteRepository utenteRepository) {
        this.itinerarioRepository = itinerarioRepository;
        this.puntoDiInteresseRepository = puntoDiInteresseRepository;
        this.utenteRepository = utenteRepository;
    }

    /**
     * Crea un nuovo itinerario basato sui dati forniti.
     *
     * @param itinerarioDTO I dati per creare l'itinerario.
     * @return L'itinerario appena creato e salvato.
     */
    @Transactional
    public Itinerario creaItinerario(ItinerarioCrea itinerarioDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Utente creatore = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Utente non trovato con username: " + username));

        Itinerario itinerario = new Itinerario();
        itinerario.setCreatore(creatore);
        itinerario.setNomeItinerario(itinerarioDTO.getNomeItinerario());
        itinerario.setDescrizione(itinerarioDTO.getDescrizione());
        itinerario.setDataPubblicazione(itinerarioDTO.getDataPubblicazione());
        itinerario.setPending(itinerarioDTO.isPending());

        return itinerarioRepository.save(itinerario);
    }

    /**
     * Aggiunge un punto di interesse a un itinerario esistente.
     *
     * @param dto I dati necessari per aggiungere il punto all'itinerario.
     */
    @Transactional
    public void aggiungiPoiAItinerario(PoiAItinerarioAggiungi dto) {
        Itinerario itinerario = itinerarioRepository.findById((long) dto.getIdItinerario())
                .orElseThrow(() -> new NoSuchElementException("Itinerario non trovato"));
        PuntoDiInteresse puntoDiInteresse = puntoDiInteresseRepository.findById((long) dto.getIdPuntoDiInteresse())
                .orElseThrow(() -> new NoSuchElementException("Punto di interesse non trovato"));

        itinerario.addPuntoDiInteresse(puntoDiInteresse);
        itinerarioRepository.save(itinerario);
    }

    /**
     * Elimina un itinerario esistente.
     *
     * @param id L'identificativo dell'itinerario da eliminare.
     * @return true se l'itinerario esiste ed è stato eliminato, false altrimenti.
     */
    @Transactional
    public boolean cancellaItinerario(Long id) {
        Itinerario itinerario = itinerarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Itinerario non trovato"));

        // Rimuovi tutte le associazioni tra l'itinerario e i punti di interesse
        itinerario.getPuntiDiInteresse().clear();
        itinerarioRepository.save(itinerario);

        // Ora puoi eliminare l'itinerario senza problemi
        itinerarioRepository.deleteById(id);
        return true;
    }

    /**
     * Approva un itinerario pendente, rendendolo disponibile a tutti gli utenti.
     *
     * @param id L'identificativo dell'itinerario da approvare.
     */
    @Transactional
    public void approvaItinerario(Long id) {
        Itinerario itinerario = itinerarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Itinerario non trovato"));
        itinerario.setPending(false);
        itinerarioRepository.save(itinerario);
    }

    /**
     * Restituisce tutti gli itinerari che sono in attesa di approvazione.
     *
     * @return Lista di itinerari pendenti.
     */
    public List<Itinerario> visualizzaItinerariDaApprovare() {
        return itinerarioRepository.findByPendingTrue();
    }

    /**
     * Restituisce un itinerario specifico tramite il suo identificativo.
     *
     * @param id L'identificativo dell'itinerario da visualizzare.
     * @return L'itinerario trovato, o null se non esiste.
     */
    public Itinerario visualizzaItinerarioById(Long id) {
        return itinerarioRepository.findById(id).orElse(null);
    }
}
