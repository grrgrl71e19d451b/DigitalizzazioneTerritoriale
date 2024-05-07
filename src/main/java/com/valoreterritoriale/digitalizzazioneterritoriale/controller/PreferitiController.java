package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Preferito;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.PreferitiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per la gestione dei preferiti.
 */
@RestController
@RequestMapping("/preferiti")
public class PreferitiController {
    private final PreferitiService preferitiService;

    /**
     * Costruttore con iniezione del servizio di gestione dei preferiti.
     *
     * @param preferitiService Servizio per la gestione dei preferiti.
     */
    public PreferitiController(PreferitiService preferitiService) {
        this.preferitiService = preferitiService;
    }

    /**
     * Aggiunge un itinerario ai preferiti dell'utente.
     *
     * @param itinerarioId Identificativo dell'itinerario da aggiungere ai preferiti.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PostMapping("/aggiungi/{itinerarioId}")
    public ResponseEntity<String> aggiungiPreferito(@PathVariable Long itinerarioId) {
        try {
            boolean success = preferitiService.aggiungiItinerarioAiPreferiti(itinerarioId);
            if (success) {
                return ResponseEntity.ok("Preferito aggiunto con successo");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile aggiungere il preferito");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell'aggiunta del preferito: " + e.getMessage());
        }
    }

    /**
     * Rimuove un itinerario dai preferiti dell'utente.
     *
     * @param itinerarioId Identificativo dell'itinerario da rimuovere dai preferiti.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @DeleteMapping("/rimuovi/{itinerarioId}")
    public ResponseEntity<String> rimuoviPreferito(@PathVariable Long itinerarioId) {
        try {
            boolean success = preferitiService.rimuoviItinerarioDaiPreferiti(itinerarioId);
            if (success) {
                return ResponseEntity.ok("Preferito rimosso con successo");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile rimuovere il preferito");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nella rimozione del preferito: " + e.getMessage());
        }
    }

    /**
     * Visualizza tutti gli itinerari aggiunti ai preferiti dall'utente.
     *
     * @return ResponseEntity con la lista dei preferiti.
     */
    @GetMapping("/visualizza")
    public ResponseEntity<List<Preferito>> visualizzaPreferiti() {
        try {
            List<Preferito> preferiti = preferitiService.visualizzaPreferiti();
            return ResponseEntity.ok(preferiti);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
