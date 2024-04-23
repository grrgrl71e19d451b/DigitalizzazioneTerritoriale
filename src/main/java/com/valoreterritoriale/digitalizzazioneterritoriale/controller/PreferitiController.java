package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.PreferitoAggiungi;
import com.valoreterritoriale.digitalizzazioneterritoriale.dto.PreferitoRimuovi;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Preferito;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.PreferitiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/preferiti")
public class PreferitiController {
    private final PreferitiService preferitiService;

    public PreferitiController(PreferitiService preferitiService) {
        this.preferitiService = preferitiService;
    }

    @PostMapping("/aggiungi")
    public ResponseEntity<String> aggiungiPreferito(@RequestBody PreferitoAggiungi dto) {
        try {
            boolean success = preferitiService.aggiungiItinerarioAiPreferiti(dto.getItinerarioId());
            if (success) {
                return ResponseEntity.ok("Preferito aggiunto con successo");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile aggiungere il preferito");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell'aggiunta del preferito: " + e.getMessage());
        }
    }

    @DeleteMapping("/rimuovi")
    public ResponseEntity<String> rimuoviPreferito(@RequestBody PreferitoRimuovi dto) {
        try {
            boolean success = preferitiService.rimuoviItinerarioDaiPreferiti(dto.getItinerarioId());
            if (success) {
                return ResponseEntity.ok("Preferito rimosso con successo");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Impossibile rimuovere il preferito");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nella rimozione del preferito: " + e.getMessage());
        }
    }
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