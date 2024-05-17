package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.ItinerarioCrea;
import com.valoreterritoriale.digitalizzazioneterritoriale.dto.PoiAItinerarioAggiungi;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Itinerario;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.UtenteRepository;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.ItinerarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per la gestione degli itinerari.
 */
@RestController
@RequestMapping("/itinerario")
public class ItinerarioController extends AbstractController {

    private final ItinerarioService itinerarioService;
    private final UtenteRepository utenteRepository;

    /**
     * Costruttore con iniezione di dipendenze per il servizio degli itinerari e il repository degli utenti.
     *
     * @param itinerarioService Servizio per la gestione degli itinerari.
     * @param utenteRepository Repository per la gestione degli utenti.
     */
    @Autowired
    public ItinerarioController(ItinerarioService itinerarioService, UtenteRepository utenteRepository) {
        this.itinerarioService = itinerarioService;
        this.utenteRepository = utenteRepository;
    }

    /**
     * Endpoint per la creazione di un nuovo itinerario.
     *
     * @param itinerarioDTO Oggetto contenente i dati necessari per la creazione di un itinerario.
     * @return ResponseEntity con l'itinerario creato o un messaggio di errore.
     */
    @PostMapping("/crea")
    public ResponseEntity<?> creaItinerario(@RequestBody ItinerarioCrea itinerarioDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Utente utenteAutenticato = utenteRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

            boolean isPendingTrue = "CONTRIBUTORE".equals(utenteAutenticato.getRuolo());
            itinerarioDTO.setPending(isPendingTrue);

            Itinerario itinerario = itinerarioService.creaItinerario(itinerarioDTO);
            return createObjectResponse(itinerario);
        } catch (Exception e) {
            return createErrorResponse("Errore durante la creazione dell'itinerario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint per aggiungere un Punto di Interesse (POI) a un itinerario esistente.
     *
     * @param dto Oggetto contenente i dati necessari per aggiungere un POI.
     * @return ResponseEntity con un messaggio di successo o un messaggio di errore.
     */
    @PutMapping("/aggiungiPoi")
    public ResponseEntity<String> aggiungiPoiAItinerario(@RequestBody PoiAItinerarioAggiungi dto) {
        try {
            itinerarioService.aggiungiPoiAItinerario(dto);
            return createSuccessResponse("Punto di interesse aggiunto all'itinerario con successo.");
        } catch (Exception e) {
            return createErrorResponse("Errore nell'aggiunta del punto di interesse all'itinerario: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint per cancellare un itinerario esistente.
     *
     * @param id Identificativo dell'itinerario da cancellare.
     * @return ResponseEntity con un messaggio di successo o un messaggio di errore.
     */
    @DeleteMapping("/cancella/{id}")
    public ResponseEntity<String> cancellaItinerario(@PathVariable Long id) {
        boolean isDeleted = itinerarioService.cancellaItinerario(id);
        if (isDeleted) {
            return createSuccessResponse("Itinerario cancellato con successo.");
        } else {
            return createErrorResponse("Itinerario non trovato.", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint per approvare un itinerario pendente.
     *
     * @param id Identificativo dell'itinerario da approvare.
     * @return ResponseEntity con un messaggio di successo o un messaggio di errore.
     */
    @PutMapping("/approva/{id}")
    public ResponseEntity<String> approvaItinerario(@PathVariable Long id) {
        try {
            itinerarioService.approvaItinerario(id);
            return createSuccessResponse("Itinerario approvato con successo.");
        } catch (Exception e) {
            return createErrorResponse("Errore nell'approvazione dell'itinerario: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint per visualizzare gli itinerari che necessitano di approvazione.
     *
     * @return ResponseEntity con la lista degli itinerari da approvare.
     */
    @GetMapping("/da-approvare")
    public ResponseEntity<List<Itinerario>> visualizzaItinerariDaApprovare() {
        List<Itinerario> itinerari = itinerarioService.visualizzaItinerariDaApprovare();
        return createListResponse(itinerari);
    }

    /**
     * Endpoint per visualizzare i dettagli di un itinerario specifico.
     *
     * @param id Identificativo dell'itinerario da visualizzare.
     * @return ResponseEntity con i dettagli dell'itinerario o un messaggio di errore se non trovato o non approvato.
     */
    @GetMapping("/visualizza/{id}")
    public ResponseEntity<?> visualizzaItinerarioById(@PathVariable Long id) {
        Itinerario itinerario = itinerarioService.visualizzaItinerarioById(id);
        if (itinerario != null && !itinerario.isPending()) {
            return createObjectResponse(itinerario);
        } else if (itinerario != null) {
            return createErrorResponse("Itinerario con ID " + id + " non è stato ancora approvato.", HttpStatus.NOT_FOUND);
        } else {
            return createErrorResponse("Itinerario con ID " + id + " non trovato.", HttpStatus.NOT_FOUND);
        }
    }
}