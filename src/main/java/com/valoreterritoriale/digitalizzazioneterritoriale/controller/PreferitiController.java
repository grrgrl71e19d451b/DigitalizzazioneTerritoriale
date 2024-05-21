package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Preferito;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.PreferitiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per la gestione dei preferiti.
 */
@RestController
@RequestMapping("/preferiti")
public class PreferitiController extends AbstractController {
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
        return (ResponseEntity<String>) create(itinerarioId, SecurityContextHolder.getContext().getAuthentication());
    }

    @Override
    protected ResponseEntity<?> create(Object request, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Long itinerarioId = (Long) request;
            try {
                boolean success = preferitiService.aggiungiItinerarioAiPreferiti(itinerarioId);
                if (success) {
                    return createSuccessResponse("Preferito aggiunto con successo");
                } else {
                    return createErrorResponse("Impossibile aggiungere il preferito", HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                return createErrorResponse("Errore nell'aggiunta del preferito: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return createErrorResponse("Non autenticato", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Rimuove un itinerario dai preferiti dell'utente.
     *
     * @param itinerarioId Identificativo dell'itinerario da rimuovere dai preferiti.
     * @param authentication informazioni sull'autenticazione dell'utente.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @DeleteMapping("/rimuovi/{itinerarioId}")
    public ResponseEntity<String> rimuoviPreferito(@PathVariable Long itinerarioId, Authentication authentication) {
        return delete(itinerarioId, authentication);
    }
    @Override
    protected ResponseEntity<String> delete(Long itinerarioId, Authentication authentication) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {

                boolean success = preferitiService.rimuoviItinerarioDaiPreferiti(itinerarioId);
                if (success) {
                    return createSuccessResponse("Preferito rimosso con successo");
                } else {
                    return createErrorResponse("Impossibile rimuovere il preferito", HttpStatus.BAD_REQUEST);
                }
            } else {
                return createErrorResponse("Non autenticato", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return createErrorResponse("Errore nella rimozione del preferito: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Visualizza tutti gli itinerari aggiunti ai preferiti dall'utente.
     *
     * @return ResponseEntity con la lista dei preferiti.
     */
    @GetMapping("/visualizza")
    public ResponseEntity<?> visualizzaPreferiti() {
        try {
            List<Preferito> preferiti = preferitiService.visualizzaPreferiti();
            return createListResponse(preferiti);
        } catch (Exception e) {
            return createErrorResponse("Errore durante la visualizzazione dei preferiti", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}