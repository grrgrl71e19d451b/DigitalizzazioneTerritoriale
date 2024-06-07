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

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Controller per la gestione dei punti di interesse.
 */
@RestController
@RequestMapping("/punti-di-interesse")
public class PuntoDiInteresseController extends AbstractController {
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
     * @param authentication oggetto Authentication che rappresenta l'autenticazione dell'utente.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PostMapping("/crea")
    public ResponseEntity<String> creaPuntoDiInteresse(@RequestBody PuntoDiInteresseCrea puntoDiInteresseDTO, Authentication authentication) {
        return (ResponseEntity<String>) create(puntoDiInteresseDTO, authentication);
    }

    @Override
    protected ResponseEntity<?> create(Object request, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            PuntoDiInteresseCrea puntoDiInteresseDTO = (PuntoDiInteresseCrea) request;
            try {
                Utente utenteAutenticato = utenteRepository.findByUsername(authentication.getName())
                        .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
                boolean isPendingTrue = "CONTRIBUTORE".equals(utenteAutenticato.getRuolo());
                puntoDiInteresseDTO.setPending(isPendingTrue);

                boolean isCreated = puntoDiInteresseService.creaPuntoDiInteresse(puntoDiInteresseDTO);
                if (isCreated) {
                    return createSuccessResponse("Punto di interesse creato con successo");
                } else {
                    return createErrorResponse("Impossibile creare il punto di interesse", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                return createErrorResponse("Errore nella creazione del punto di interesse: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return createErrorResponse("Non autenticato", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Metodo per cancellare un punto di interesse dato il suo ID.
     * @param id l'ID del punto di interesse da cancellare.
     * @param authentication informazioni sull'autenticazione dell'utente.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @DeleteMapping("/cancella/{id}")
    public ResponseEntity<String> cancellaPuntoDiInteresse(@PathVariable Long id, Authentication authentication) {
        return delete(id, authentication);
    }

    @Override
    protected ResponseEntity<String> delete(Long id, Authentication authentication) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {

                boolean isDeleted = puntoDiInteresseService.cancellaPuntoDiInteresse(id);
                if (isDeleted) {
                    return createSuccessResponse("Punto di interesse cancellato con successo");
                } else {
                    return createErrorResponse("Punto di interesse non trovato", HttpStatus.NOT_FOUND);
                }
            } else {
                return createErrorResponse("Non autenticato", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            return createErrorResponse("Si è verificato un errore durante la cancellazione del punto di interesse.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Metodo per visualizzare i dettagli di un punto di interesse tramite il suo ID.
     * @param id l'ID del punto di interesse.
     * @param authentication le informazioni di autenticazione dell'utente.
     * @return ResponseEntity con il punto di interesse o un messaggio di errore se non trovato.
     */
    @GetMapping("/visualizza/{id}")
    public ResponseEntity<?> visualizzaPuntoDiInteresseById(@PathVariable Long id, Authentication authentication) {
        return read(id, authentication);
    }

    @Override
    protected ResponseEntity<?> read(Long id, Authentication authentication) {
        PuntoDiInteresse puntoDiInteresse = puntoDiInteresseService.visualizzaPuntoDiInteresseById(id);
        if (puntoDiInteresse != null) {
            if (!puntoDiInteresse.isPending()) {
                return createObjectResponse(puntoDiInteresse);
            } else {
                return createErrorResponse("Punto di interesse con ID " + id + " è in attesa di approvazione.", HttpStatus.NOT_FOUND);
            }
        } else {
            return createErrorResponse("Punto di interesse con ID " + id + " non trovato.", HttpStatus.NOT_FOUND);
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
    public ResponseEntity<String> approvaPuntoDiInteresse(@PathVariable Long id, Authentication authentication) {
        return update(id, null, authentication);
    }

    @Override
    protected ResponseEntity<String> update(Long id, Object request, Authentication authentication) {
        try {
            puntoDiInteresseService.approvaPuntoDiInteresse(id);
            return createSuccessResponse("Punto di interesse approvato con successo");
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
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
            return createErrorResponse("Punto di interesse non trovato.", HttpStatus.NOT_FOUND);
        }

        if (!puntoDiInteresse.isPending()) {
            String osmData = fetchOsmData(puntoDiInteresse.getLatitudine(), puntoDiInteresse.getLongitudine());
            return createObjectResponse(osmData);
        } else {
            return createErrorResponse("Punto di interesse con ID " + id + " è in attesa di approvazione.", HttpStatus.NOT_FOUND);
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