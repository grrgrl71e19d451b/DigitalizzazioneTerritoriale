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

@RestController
@RequestMapping("/itinerario")
public class ItinerarioController {

    private final ItinerarioService itinerarioService;
    private final UtenteRepository utenteRepository;

    @Autowired
    public ItinerarioController(ItinerarioService itinerarioService, UtenteRepository utenteRepository) {
        this.itinerarioService = itinerarioService;
        this.utenteRepository = utenteRepository;
    }

    @PostMapping("/crea")
    public ResponseEntity<Itinerario> creaItinerario(@RequestBody ItinerarioCrea itinerarioDTO) {
        try {
            // Ottieni l'utente autenticato
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Utente utenteAutenticato = utenteRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

            // Imposta il valore di pending in base al ruolo dell'utente autenticato
            boolean isPendingTrue = "CONTRIBUTORE".equals(utenteAutenticato.getRuolo());
            itinerarioDTO.setPending(isPendingTrue);

            Itinerario itinerario = itinerarioService.creaItinerario(itinerarioDTO);
            return ResponseEntity.ok(itinerario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/aggiungiPoi")
    public ResponseEntity<?> aggiungiPoiAItinerario(@RequestBody PoiAItinerarioAggiungi dto) {
        try {
            itinerarioService.aggiungiPoiAItinerario(dto);
            return ResponseEntity.ok("Punto di interesse aggiunto all'itinerario con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore nell'aggiunta del punto di interesse all'itinerario: " + e.getMessage());
        }
    }

    @DeleteMapping("/cancella/{id}")
    public ResponseEntity<?> cancellaItinerario(@PathVariable Long id) {
        boolean isDeleted = itinerarioService.cancellaItinerario(id);
        if (isDeleted) {
            return ResponseEntity.ok("Itinerario cancellato con successo.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Itinerario non trovato.");
        }
    }

    @PutMapping("/approva/{id}")
    public ResponseEntity<?> approvaItinerario(@PathVariable Long id) {
        try {
            itinerarioService.approvaItinerario(id);
            return ResponseEntity.ok("Itinerario approvato con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Errore nell'approvazione dell'itinerario: " + e.getMessage());
        }
    }

    @GetMapping("/da-approvare")
    public ResponseEntity<List<Itinerario>> visualizzaItinerariDaApprovare() {
        List<Itinerario> itinerari = itinerarioService.visualizzaItinerariDaApprovare();
        return ResponseEntity.ok(itinerari);
    }

    @GetMapping("/visualizza/{id}")
    public ResponseEntity<?> visualizzaItinerarioById(@PathVariable Long id) {
        Itinerario itinerario = itinerarioService.visualizzaItinerarioById(id);
        if (itinerario != null && !itinerario.isPending()) {
            return ResponseEntity.ok(itinerario);
        } else if (itinerario != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Itinerario con ID " + id + " non è stato ancora approvato.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Itinerario con ID " + id + " non trovato.");
        }
    }
}
