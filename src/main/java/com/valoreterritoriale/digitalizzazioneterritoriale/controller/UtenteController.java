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
public class UtenteController {

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
    public ResponseEntity<?> creaUtente(@RequestBody UtenteCrea utenteCrea, Authentication authentication) {
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
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non autorizzato a creare utenti");
            }
        } else {
            if (utenteCrea.getRuolo().equals("TURISTAAUTENTICATO") || utenteCrea.getRuolo().equals("CONTRIBUTORE")) {
                utente.setRuolo(utenteCrea.getRuolo());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Non autorizzato ad assegnare altri ruoli per utenti non autenticati");
            }
        }

        utenteService.creaUtente(utente);
        return ResponseEntity.ok("Utente creato con successo");
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

            return ResponseEntity.ok("La tua richiesta di modifica del ruolo è stata inviata al GESTOREPIATTAFORMA per l'approvazione.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'invio della richiesta di modifica del ruolo: " + e.getMessage());
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
            return ResponseEntity.ok(utente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
        }
    }

    /**
     * Metodo per modificare il ruolo di un utente.
     * @param id l'ID dell'utente da modificare.
     * @param ruoloModifica oggetto RuoloModifica contenente il nuovo ruolo.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PutMapping("/modifica-ruolo/{id}")
    public ResponseEntity<?> modificaRuoloUtente(@PathVariable Long id, @RequestBody RuoloModifica ruoloModifica) {
        try {
            String nuovoRuolo = ruoloModifica.getNuovoRuolo();
            Utente updatedUtente = utenteService.modificaRuoloUtente(id, nuovoRuolo);
            return ResponseEntity.ok("Ruolo aggiornato con successo: " + updatedUtente.getRuolo());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Errore: " + e.getMessage());
        }
    }

    /**
     * Metodo per cancellare un utente.
     * @param id l'ID dell'utente da cancellare.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @DeleteMapping("/cancella/{id}")
    public ResponseEntity<?> cancellaUtente(@PathVariable Long id) {
        try {
            boolean isDeleted = utenteService.cancellaUtente(id);
            if (isDeleted) {
                return ResponseEntity.ok("Utente cancellato con successo");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utente non trovato");
            }
        } catch (Exception ex) {
            // Gestisci qualsiasi eccezione lanciata dal service
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Si è verificato un errore durante la cancellazione dell'utente.");
        }
    }
}
