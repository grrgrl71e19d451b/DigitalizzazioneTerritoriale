package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.RichiestaModificaRuolo;
import com.valoreterritoriale.digitalizzazioneterritoriale.dto.RuoloModifica;
import com.valoreterritoriale.digitalizzazioneterritoriale.dto.UtenteCrea;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.EmailService;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.UtenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Controller per la gestione degli utenti.
 */
@RestController
@RequestMapping("/utente")
public class UtenteController extends AbstractController {

    private final UtenteService utenteService;
    private final EmailService emailService;

    /**
     * Costruttore della classe UtenteController.
     * @param utenteService servizio per la gestione degli utenti.
     * @param emailService servizio per l'invio di email.
     */
    public UtenteController(UtenteService utenteService, EmailService emailService) {
        this.utenteService = utenteService;
        this.emailService = emailService;
    }

    /**
     * Metodo per creare un nuovo utente.
     * @param utenteCrea oggetto UtenteCrea contenente i dettagli dell'utente da creare.
     * @param authentication informazioni sull'autenticazione dell'utente.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PostMapping("/crea")
    public ResponseEntity<String> creaUtente(@RequestBody UtenteCrea utenteCrea, Authentication authentication) {
        return create(utenteCrea, authentication);
    }

    @Override
    protected ResponseEntity<String> create(Object request, Authentication authentication) {
        UtenteCrea utenteCrea = (UtenteCrea) request;
        Utente utente = new Utente();
        utente.setNome(utenteCrea.getNome());
        utente.setCognome(utenteCrea.getCognome());
        utente.setEmail(utenteCrea.getEmail());
        utente.setComune(utenteCrea.getComune());
        utente.setEta(utenteCrea.getEta());
        utente.setUsername(utenteCrea.getUsername());
        utente.setPassword(utenteCrea.getPassword());

        if (authentication != null && authentication.isAuthenticated()) {
            String ruoloAutenticato = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining());

            if (ruoloAutenticato.contains("GESTOREPIATTAFORMA")) {
                utente.setRuolo(utenteCrea.getRuolo());
            } else {
                return createErrorResponse("Non autorizzato a creare utenti", HttpStatus.FORBIDDEN);
            }
        } else {
            if (utenteCrea.getRuolo().equals("TURISTAAUTENTICATO") || utenteCrea.getRuolo().equals("CONTRIBUTORE")) {
                utente.setRuolo(utenteCrea.getRuolo());
            } else {
                return createErrorResponse("Non autorizzato ad assegnare altri ruoli per utenti non autenticati", HttpStatus.FORBIDDEN);
            }
        }

        utenteService.creaUtente(utente);
        return createSuccessResponse("Utente creato con successo");
    }

    /**
     * Metodo per inviare una richiesta di modifica del ruolo.
     * @param richiestaModificaRuolo oggetto RichiestaModificaRuolo contenente i dettagli della richiesta.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PostMapping("/richiediModificaRuolo")
    public ResponseEntity<String> richiediModificaRuolo(@RequestBody RichiestaModificaRuolo richiestaModificaRuolo) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            emailService.sendRuoloModificaEmail(username, richiestaModificaRuolo);

            return createSuccessResponse("La tua richiesta di modifica del ruolo è stata inviata al GESTOREPIATTAFORMA per l'approvazione.");
        } catch (Exception e) {
            return createErrorResponse("Errore durante l'invio della richiesta di modifica del ruolo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Metodo per visualizzare i dettagli di un utente.
     * @param id l'ID dell'utente da visualizzare.
     * @return ResponseEntity con l'utente o un messaggio di errore se non trovato.
     */
    @GetMapping("/visualizza/{id}")
    public ResponseEntity<?> visualizzaUtente(@PathVariable Long id) {
        Utente utente = utenteService.visualizzaUtente(id);
        if (utente != null) {
            return createObjectResponse(utente);
        } else {
            return createErrorResponse("Utente non trovato", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Metodo per modificare il ruolo di un utente.
     * @param id l'ID dell'utente da modificare.
     * @param ruoloModifica oggetto RuoloModifica contenente il nuovo ruolo.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PutMapping("/modifica-ruolo/{id}")
    public ResponseEntity<String> modificaRuoloUtente(@PathVariable Long id, @RequestBody RuoloModifica ruoloModifica) {
        try {
            String nuovoRuolo = ruoloModifica.getNuovoRuolo();
            Utente updatedUtente = utenteService.modificaRuoloUtente(id, nuovoRuolo);
            return createSuccessResponse("Ruolo aggiornato con successo: " + updatedUtente.getRuolo());
        } catch (RuntimeException e) {
            return createErrorResponse("Errore: " + e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Metodo per cancellare un utente.
     * @param id l'ID dell'utente da cancellare.
     * @param authentication informazioni sull'autenticazione dell'utente.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @DeleteMapping("/cancella/{id}")
    public ResponseEntity<String> cancellaUtente(@PathVariable Long id, Authentication authentication) {
        return delete(id, authentication);
    }

    @Override
    protected ResponseEntity<String> delete(Long id, Authentication authentication) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                String ruoloAutenticato = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining());

                if (ruoloAutenticato.contains("GESTOREPIATTAFORMA")) {
                    boolean isDeleted = utenteService.cancellaUtente(id);
                    if (isDeleted) {
                        return new ResponseEntity<>("Utente cancellato con successo", HttpStatus.NO_CONTENT);
                    } else {
                        return new ResponseEntity<>("Utente non trovato", HttpStatus.NOT_FOUND);
                    }
                } else {
                    return new ResponseEntity<>("Non autorizzato a cancellare utenti", HttpStatus.FORBIDDEN);
                }
            } else {
                return new ResponseEntity<>("Non autenticato", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            // Gestisce qualsiasi eccezione lanciata dal service
            return new ResponseEntity<>("Si è verificato un errore durante la cancellazione dell'utente.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
