package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.RuoloModifica;
import com.valoreterritoriale.digitalizzazioneterritoriale.dto.UtenteCrea;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.UtenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/utente")
public class UtenteController {

    private final UtenteService utenteService;

    // Constructor injection
    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping("/crea")
    public ResponseEntity<?> creaUtente(@RequestBody UtenteCrea utenteDTO) {
        Utente utente = new Utente();
        utente.setNome(utenteDTO.getNome());
        utente.setCognome(utenteDTO.getCognome());
        utente.setEmail(utenteDTO.getEmail());
        utente.setComune(utenteDTO.getComune());
        utente.setRuolo(utenteDTO.getRuolo());
        utente.setEta(utenteDTO.getEta());

        utenteService.creaUtente(utente);
        return ResponseEntity.ok("Utente creato con successo");
    }

    @GetMapping("/visualizza/{id}")
    public ResponseEntity<?> visualizzaUtente(@PathVariable Integer id) {
        Utente utente = utenteService.visualizzaUtente(id);
        if (utente != null) {
            return ResponseEntity.ok(utente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
        }
    }

    @PutMapping("/modifica-ruolo/{id}")
    public ResponseEntity<?> modificaRuoloUtente(@PathVariable Integer id, @RequestBody RuoloModifica ruoloDTO) {
        try {
            String nuovoRuolo = ruoloDTO.getNuovoRuolo();
            Utente updatedUtente = utenteService.modificaRuoloUtente(id, nuovoRuolo);
            return ResponseEntity.ok("Ruolo aggiornato con successo: " + updatedUtente.getRuolo());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Errore: " + e.getMessage());
        }
    }


    @DeleteMapping("/cancella/{id}")
    public ResponseEntity<?> cancellaUtente(@PathVariable Integer id) {
        boolean isDeleted = utenteService.cancellaUtente(id);
        if (isDeleted) {
            return ResponseEntity.ok("Utente cancellato con successo");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
        }
    }
}
