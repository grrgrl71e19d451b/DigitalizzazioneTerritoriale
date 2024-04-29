package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.PuntoDiInteresseCrea;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.PuntoDiInteresse;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.UtenteRepository;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.PuntoDiInteresseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/punti-di-interesse")
public class PuntoDiInteresseController {
    private final PuntoDiInteresseService puntoDiInteresseService;
    private final UtenteRepository utenteRepository;

    @Autowired
    public PuntoDiInteresseController(PuntoDiInteresseService puntoDiInteresseService, UtenteRepository utenteRepository) {
        this.puntoDiInteresseService = puntoDiInteresseService;
        this.utenteRepository = utenteRepository;
    }

    @PostMapping("/crea")
    public ResponseEntity<?> creaPuntoDiInteresse(@RequestBody PuntoDiInteresseCrea puntoDiInteresseDTO) {
        try {
            // Ottieni l'utente autenticato
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Utente utenteAutenticato = utenteRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

            // Imposta il valore di pending in base al ruolo dell'utente autenticato
            boolean isPendingTrue = "CONTRIBUTORE".equals(utenteAutenticato.getRuolo());
            puntoDiInteresseDTO.setPending(isPendingTrue);

            boolean isCreated = puntoDiInteresseService.creaPuntoDiInteresse(puntoDiInteresseDTO);
            if (isCreated) {
                return ResponseEntity.ok("Punto di interesse creato con successo");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile creare il punto di interesse");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nella creazione del punto di interesse: " + e.getMessage());
        }
    }

    @DeleteMapping("/cancella/{id}")
    public ResponseEntity<?> cancellaPuntoDiInteresse(@PathVariable Long id) {
        boolean isDeleted = puntoDiInteresseService.cancellaPuntoDiInteresse(id);
        if (isDeleted) {
            return ResponseEntity.ok("Punto di interesse cancellato con successo");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Punto di interesse non trovato");
        }
    }

    @GetMapping("/visualizza/{id}")
    public ResponseEntity<?> visualizzaPuntoDiInteresseById(@PathVariable Long id) {
        PuntoDiInteresse puntoDiInteresse = puntoDiInteresseService.visualizzaPuntoDiInteresseById(id);
        if (puntoDiInteresse != null) {
            if (!puntoDiInteresse.isPending()) {
                return ResponseEntity.ok(puntoDiInteresse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Punto di interesse con ID " + id + " è in attesa di approvazione.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Punto di interesse con ID " + id + " non trovato.");
        }
    }

    @GetMapping("/pending")
    public List<PuntoDiInteresse> visualizzaPendingPuntiDiInteresse() {
        return puntoDiInteresseService.visualizzaPendingPuntiDiInteresse();
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<String> approvaPuntoDiInteresse(@PathVariable Long id) {
        try {
            puntoDiInteresseService.approvaPuntoDiInteresse(id);
            return ResponseEntity.ok("Punto di interesse approvato con successo");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
