package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.PuntoDiInteresseCrea;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.PuntoDiInteresse;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.PuntoDiInteresseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/punti-di-interesse")
public class PuntoDiInteresseController {
    private final PuntoDiInteresseService puntoDiInteresseService;

    @Autowired
    public PuntoDiInteresseController(PuntoDiInteresseService puntoDiInteresseService) {
        this.puntoDiInteresseService = puntoDiInteresseService;
    }

    @PostMapping
    public ResponseEntity<?> createPuntoDiInteresse(@RequestBody PuntoDiInteresseCrea puntoDiInteresseDTO) {
        boolean isCreated = puntoDiInteresseService.createPuntoDiInteresse(puntoDiInteresseDTO);
        if (isCreated) {
            return ResponseEntity.ok("Punto di interesse creato con successo");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile creare il punto di interesse");
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePuntoDiInteresse(@PathVariable Long id) {
        boolean isDeleted = puntoDiInteresseService.deletePuntoDiInteresse(id);
        if (isDeleted) {
            return ResponseEntity.ok("Punto di interesse cancellato con successo");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Punto di interesse non trovato");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPuntoDiInteresseById(@PathVariable Long id) {
        PuntoDiInteresse puntoDiInteresse = puntoDiInteresseService.findPuntoDiInteresseById(id);
        if (puntoDiInteresse != null) {
            return ResponseEntity.ok(puntoDiInteresse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/pending")
    public List<PuntoDiInteresse> getPendingPuntiDiInteresse() {
        return puntoDiInteresseService.findAllPendingPuntiDiInteresse();
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<String> approvePuntoDiInteresse(@PathVariable Long id) {
        try {
            puntoDiInteresseService.approvePuntoDiInteresse(id);
            return ResponseEntity.ok("Punto di interesse approvato con successo");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
