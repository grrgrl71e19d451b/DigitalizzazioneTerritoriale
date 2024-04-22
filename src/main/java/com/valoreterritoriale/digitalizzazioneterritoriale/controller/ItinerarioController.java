package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.ItinerarioCrea;
import com.valoreterritoriale.digitalizzazioneterritoriale.dto.PoiAItinerarioAggiungi;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Itinerario;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.ItinerarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itinerario")
public class ItinerarioController {

    private final ItinerarioService itinerarioService;

    public ItinerarioController(ItinerarioService itinerarioService) {
        this.itinerarioService = itinerarioService;
    }

    @PostMapping("/crea")
    public ResponseEntity<Itinerario> creaItinerario(@RequestBody ItinerarioCrea itinerarioDTO) {
        Itinerario itinerario = itinerarioService.creaItinerario(itinerarioDTO);
        return ResponseEntity.ok(itinerario);
    }

    @PutMapping("/aggiungiPoi")
    public ResponseEntity<?> aggiungiPoiAItinerario(@RequestBody PoiAItinerarioAggiungi dto) {
        itinerarioService.aggiungiPoiAItinerario(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancella/{id}")
    public ResponseEntity<?> cancellaItinerario(@PathVariable Long id) {
        itinerarioService.cancellaItinerario(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/approva/{id}")
    public ResponseEntity<?> approvaItinerario(@PathVariable Long id) {
        itinerarioService.approvaItinerario(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/da-approvare")
    public ResponseEntity<List<Itinerario>> visualizzaItinerariDaApprovare() {
        List<Itinerario> itinerari = itinerarioService.visualizzaItinerariDaApprovare();
        return ResponseEntity.ok(itinerari);
    }

    @GetMapping("/visualizza/{id}")
    public ResponseEntity<?> getItinerarioById(@PathVariable Long id) {
        Itinerario itinerario = itinerarioService.findItinerarioById(id);
        if (itinerario != null) {
            return ResponseEntity.ok(itinerario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}