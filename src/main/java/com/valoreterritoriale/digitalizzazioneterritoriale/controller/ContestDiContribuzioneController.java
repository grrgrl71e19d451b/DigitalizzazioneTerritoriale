package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.ContestDiContribuzioneCrea;
import com.valoreterritoriale.digitalizzazioneterritoriale.dto.ContestUtenteInvita;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.ContestDiContribuzione;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Partecipazione;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.UtenteRepository;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.ContestDiContribuzioneService;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.EmailService;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.PartecipazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller per la gestione dei contest di contribuzione.
 */
@RestController
@RequestMapping("/contest-di-contribuzione")
public class ContestDiContribuzioneController extends AbstractController {
    private final ContestDiContribuzioneService contestService;
    private final UtenteRepository utenteRepository;
    private final PartecipazioneService partecipazioneService;
    private final EmailService emailService;

    /**
     * Costruttore per l'iniezione delle dipendenze necessarie.
     *
     * @param contestService Servizio per la gestione dei contest di contribuzione.
     * @param utenteRepository Repository per la gestione degli utenti.
     * @param partecipazioneService Servizio per la gestione delle partecipazioni ai contest.
     */
    @Autowired
    public ContestDiContribuzioneController(ContestDiContribuzioneService contestService,
                                            UtenteRepository utenteRepository,
                                            PartecipazioneService partecipazioneService,
                                            EmailService emailService) {
        this.contestService = contestService;
        this.utenteRepository = utenteRepository;
        this.partecipazioneService = partecipazioneService;
        this.emailService = emailService;
    }

    /**
     * Endpoint per la creazione di un contest di contribuzione.
     *
     * @param contestDiContribuzioneDTO Oggetto che contiene i dati per la creazione del contest.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PostMapping("/crea")
    public ResponseEntity<?> creaContestDiContribuzione(@RequestBody ContestDiContribuzioneCrea contestDiContribuzioneDTO) {
        return create(contestDiContribuzioneDTO, SecurityContextHolder.getContext().getAuthentication());
    }

    @Override
    protected ResponseEntity<?> create(Object request, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            ContestDiContribuzioneCrea contestDiContribuzioneDTO = (ContestDiContribuzioneCrea) request;
            try {
                Utente utenteAutenticato = utenteRepository.findByUsername(authentication.getName())
                        .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
                contestDiContribuzioneDTO.setCreatore(utenteAutenticato);

                boolean isCreated = contestService.creaContestDiContribuzione(contestDiContribuzioneDTO);
                if (isCreated) {
                    return createSuccessResponse("Contest di contribuzione creato con successo");
                } else {
                    return createErrorResponse("Impossibile creare il contest di contribuzione", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                return createErrorResponse("Errore nella creazione del contest di contribuzione: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return createErrorResponse("Non autenticato", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Endpoint per invitare un utente a partecipare a un contest di contribuzione.
     *
     * @param contestId Identificativo del contest.
     * @param utenteInvita Dettagli dell'utente da invitare.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PostMapping("/invita/{contestId}")
    public ResponseEntity<String> invitaUtente(@PathVariable Long contestId, @RequestBody ContestUtenteInvita utenteInvita) {
        try {
            if (contestService.trovaContestDiContribuzionePerId(contestId).isEmpty()) {
                return createErrorResponse("Contest non trovato", HttpStatus.NOT_FOUND);
            }

            final String CODICE_UNIVERSALE = "XYZ123";

            // Invia l'email utilizzando l'email fornita nel DTO
            emailService.sendContestPartecipationEmail(utenteInvita.getName(), utenteInvita.getEmail(), contestId, CODICE_UNIVERSALE);

            return createSuccessResponse("Invito inviato a " + utenteInvita.getName() + " con il codice di partecipazione.");
        } catch (Exception e) {
            return createErrorResponse("Errore nell'invio dell'invito: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint per la partecipazione a un contest di contribuzione.
     *
     * @param contestId Identificativo del contest.
     * @param codicePartecipazione Codice necessario per partecipare.
     * @param file File opzionale contenente materiale per il contest.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PostMapping("/partecipaAlContest")
    public ResponseEntity<String> partecipaAlContest(
            @RequestParam("contestId") Long contestId,
            @RequestParam("codicePartecipazione") String codicePartecipazione,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Utente utenteAutenticato = utenteRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

            if (!partecipazioneService.verificaCodice(codicePartecipazione, contestId)) {
                return createErrorResponse("Codice di partecipazione non valido o contest non trovato.", HttpStatus.UNAUTHORIZED);
            }

            byte[] fileBytes = (file != null) ? file.getBytes() : null;
            partecipazioneService.aggiungiPartecipanteAlContest(utenteAutenticato.getId(), contestId, codicePartecipazione, fileBytes);

            return createSuccessResponse("Partecipazione al contest confermata. Il materiale inviato è in attesa di validazione.");
        } catch (Exception e) {
            return createErrorResponse("Errore durante la registrazione al contest: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint per la visualizzazione del materiale inviato dai partecipanti a un contest.
     *
     * @param contestId Identificativo del contest.
     * @return ResponseEntity con l'elenco delle partecipazioni.
     */
    @GetMapping("/visionaMaterialePartecipanti/{contestId}")
    public ResponseEntity<?> visionaMaterialePartecipanti(@PathVariable Long contestId) {
        try {
            List<Partecipazione> partecipazioni = partecipazioneService.trovaPartecipazioniPerContestId(contestId);
            return createListResponse(partecipazioni);
        } catch (Exception e) {
            return createErrorResponse("Errore nella visualizzazione del materiale dei partecipanti: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint per approvare la partecipazione di un utente a un contest.
     *
     * @param partecipazioneId Identificativo della partecipazione da approvare.
     * @return ResponseEntity con il risultato dell'operazione.
     */
    @PutMapping("/approvaPartecipazione/{partecipazioneId}")
    public ResponseEntity<String> approvaPartecipazione(@PathVariable Long partecipazioneId) {
        try {
            partecipazioneService.approvaPartecipazione(partecipazioneId);
            return createSuccessResponse("Partecipazione approvata con successo");
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint per mostrare i dettagli di un contest di contribuzione.
     *
     * @param id Identificativo del contest.
     * @param authentication le informazioni di autenticazione dell'utente.
     * @return ResponseEntity con i dettagli del contest o stato not found se non esiste.
     */
    @GetMapping("/mostra/{id}")
    public ResponseEntity<?> mostraContestDiContribuzione(@PathVariable Long id, Authentication authentication) {
        return read(id, authentication);
    }

    @Override
    protected ResponseEntity<ContestDiContribuzione> read(Long id, Authentication authentication) {
        return contestService.trovaContestDiContribuzionePerId(id)
                .map(this::createObjectResponse)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * Endpoint per cancellare un contest di contribuzione.
     *
     * @param id Identificativo del contest da cancellare.
     * @param authentication informazioni sull'autenticazione dell'utente.
     * @return ResponseEntity con un messaggio di successo o un messaggio di errore.
     */
    @DeleteMapping("/cancella/{id}")
    public ResponseEntity<String> cancellaContestDiContribuzione(@PathVariable Long id, Authentication authentication) {
        return delete(id, authentication);
    }

    @Override
    protected ResponseEntity<String> delete(Long id, Authentication authentication) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {

                contestService.cancellaContestDiContribuzionePerId(id);
                return createSuccessResponse("Contest cancellato con successo.");
            } else {
                return createErrorResponse("Non autenticato", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            return createErrorResponse("Si è verificato un errore durante la cancellazione del contest: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Questo metodo non è attualmente implementato, ma è disponibile per future estensioni.
     * @param id Identificativo della risorsa da aggiornare.
     * @param request Oggetto della richiesta contenente i dati aggiornati.
     * @param authentication Informazioni sull'autenticazione dell'utente.
     * @return ResponseEntity che incapsula l'esito dell'operazione di aggiornamento.
     */
    @Override
    protected <T> ResponseEntity<T> update(Long id, Object request, Authentication authentication) {
        return null;
    }
}