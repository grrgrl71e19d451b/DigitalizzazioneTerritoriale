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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Controller per la gestione dei punti di interesse.
 */
@RestController
@RequestMapping("/punti-di-interesse")
public class PuntoDiInteresseController {
    private final PuntoDiInteresseService puntoDiInteresseService;
    private final UtenteRepository utenteRepository;
    private final RestTemplate restTemplate;

    /**
     * Costruttore della classe PuntoDiInteresseController.
     * @param puntoDiInteresseService servizio per la gestione dei punti di interesse.
     * @param utenteRepository repository per la gestione degli utenti.
     * @param restTemplate utilizzato per le richieste HTTP esterne.
     */
    @Autowired
    public PuntoDiInteresseController(PuntoDiInteresseService puntoDiInteresseService, UtenteRepository utenteRepository, RestTemplate restTemplate) {
        this.puntoDiInteresseService = puntoDiInteresseService;
        this.utenteRepository = utenteRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Metodo per creare un nuovo punto di interesse.
     * @param puntoDiInteresseDTO oggetto DTO per la creazione di un punto di interesse.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PostMapping("/crea")
    public ResponseEntity<?> creaPuntoDiInteresse(@RequestBody PuntoDiInteresseCrea puntoDiInteresseDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Utente utenteAutenticato = utenteRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
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

    /**
     * Metodo per cancellare un punto di interesse dato il suo ID.
     * @param id l'ID del punto di interesse da cancellare.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @DeleteMapping("/cancella/{id}")
    public ResponseEntity<?> cancellaPuntoDiInteresse(@PathVariable Long id) {
        boolean isDeleted = puntoDiInteresseService.cancellaPuntoDiInteresse(id);
        if (isDeleted) {
            return ResponseEntity.ok("Punto di interesse cancellato con successo");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Punto di interesse non trovato");
        }
    }

    /**
     * Metodo per visualizzare i dettagli di un punto di interesse tramite il suo ID.
     * @param id l'ID del punto di interesse.
     * @return ResponseEntity con il punto di interesse o un messaggio di errore se non trovato.
     */
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

    /**
     * Metodo per ottenere una lista di punti di interesse che sono in attesa di approvazione.
     * @return Lista di punti di interesse pendenti.
     */
    @GetMapping("/pending")
    public List<PuntoDiInteresse> visualizzaPendingPuntiDiInteresse() {
        return puntoDiInteresseService.visualizzaPendingPuntiDiInteresse();
    }

    /**
     * Metodo per approvare un punto di interesse dato il suo ID.
     * @param id l'ID del punto di interesse da approvare.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PutMapping("/approve/{id}")
    public ResponseEntity<String> approvaPuntoDiInteresse(@PathVariable Long id) {
        try {
            puntoDiInteresseService.approvaPuntoDiInteresse(id);
            return ResponseEntity.ok("Punto di interesse approvato con successo");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Metodo per ottenere informazioni da OpenStreetMap basate sulla latitudine e longitudine di un punto di interesse.
     * @param id l'ID del punto di interesse.
     * @return ResponseEntity con i dati di OpenStreetMap o un messaggio di errore se il punto di interesse non è trovato o in attesa di approvazione.
     */
    @GetMapping("/osm-info/{id}")
    public ResponseEntity<?> getOsmInfoById(@PathVariable Long id) {
        PuntoDiInteresse puntoDiInteresse = puntoDiInteresseService.visualizzaPuntoDiInteresseById(id);
        if (puntoDiInteresse == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Punto di interesse non trovato.");
        }

        if (!puntoDiInteresse.isPending()) {
            String osmData = fetchOsmData(puntoDiInteresse.getLatitudine(), puntoDiInteresse.getLongitudine());
            return ResponseEntity.ok(osmData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Punto di interesse con ID " + id + " è in attesa di approvazione.");
        }
    }

    /**
     * Metodo privato per effettuare una richiesta HTTP a OpenStreetMap e recuperare dati basati su latitudine e longitudine.
     * @param latitude la latitudine del punto di interesse.
     * @param longitude la longitudine del punto di interesse.
     * @return String contenente i dati JSON di OpenStreetMap.
     */
    private String fetchOsmData(double latitude, double longitude) {
        String uri = UriComponentsBuilder.fromHttpUrl("https://nominatim.openstreetmap.org/reverse")
                .queryParam("format", "json")
                .queryParam("lat", latitude)
                .queryParam("lon", longitude)
                .toUriString();

        return restTemplate.getForObject(uri, String.class);
    }
}
