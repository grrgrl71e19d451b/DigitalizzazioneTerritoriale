package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.ContestDiContribuzioneCrea;
import com.valoreterritoriale.digitalizzazioneterritoriale.dto.ContestUtenteInvita;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.ContestDiContribuzione;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Partecipazione;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.UtenteRepository;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.ContestDiContribuzioneService;
import com.valoreterritoriale.digitalizzazioneterritoriale.service.PartecipazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/contest-di-contribuzione")
public class ContestDiContribuzioneController {
    private final ContestDiContribuzioneService contestService;
    private final UtenteRepository utenteRepository;
    private final JavaMailSender mailSender;
    private final PartecipazioneService partecipazioneService;

    @Autowired
    public ContestDiContribuzioneController(ContestDiContribuzioneService contestService, UtenteRepository utenteRepository, JavaMailSender mailSender, PartecipazioneService partecipazioneService) {
        this.contestService = contestService;
        this.utenteRepository = utenteRepository;
        this.mailSender = mailSender;
        this.partecipazioneService = partecipazioneService;
    }

    @PostMapping("/crea")
    public ResponseEntity<?> creaContestDiContribuzione(@RequestBody ContestDiContribuzioneCrea contestDiContribuzioneCrea) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Utente utenteAutenticato = utenteRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

            contestDiContribuzioneCrea.setCreatore(utenteAutenticato);

            boolean isCreated = contestService.creaContestDiContribuzione(contestDiContribuzioneCrea);
            if (isCreated) {
                return ResponseEntity.ok("Contest di contribuzione creato con successo");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile creare il contest di contribuzione");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nella creazione del contest di contribuzione: " + e.getMessage());
        }
    }

    @PostMapping("/invita/{contestId}")
    public ResponseEntity<String> invitaUtente(@PathVariable Long contestId, @RequestBody ContestUtenteInvita utenteInvita) {
        try {
            ContestDiContribuzione contest = contestService.trovaContestDiContribuzionePerId(contestId)
                    .orElseThrow(() -> new IllegalArgumentException("Contest non trovato"));

            // Codice universale
            final String CODICE_UNIVERSALE = "XYZ123";

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("noreply@example.com");
            mailMessage.setTo(utenteInvita.getEmail());
            mailMessage.setSubject("Invito al Contest: " + contest.getNome());

            // Includi la descrizione del contest e il codice di partecipazione nel corpo dell'email
            String mailText = String.format(
                    "Ciao %s,\n\nSei stato invitato a partecipare al contest '%s'.\n\nDescrizione: %s\n\nIl contest si terrà il %s. Per partecipare, usa il seguente codice di partecipazione: %s\n\nVisita il nostro sito per maggiori dettagli e per confermare la tua partecipazione.",
                    utenteInvita.getName(), contest.getNome(), contest.getDescrizione(), contest.getDataEvento().toString(), CODICE_UNIVERSALE
            );

            mailMessage.setText(mailText);
            mailSender.send(mailMessage);

            return ResponseEntity.ok("Invito inviato a " + utenteInvita.getName() + " con il codice di partecipazione.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nell'invio dell'invito: " + e.getMessage());
        }
    }

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
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Codice di partecipazione non valido o contest non trovato.");
            }

            byte[] fileBytes = (file != null) ? file.getBytes() : null;
            partecipazioneService.aggiungiPartecipanteAlContest(utenteAutenticato.getId(), contestId, codicePartecipazione, fileBytes);

            return ResponseEntity.ok("Partecipazione al contest confermata. Il materiale inviato è in attesa di validazione.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante la registrazione al contest: " + e.getMessage());
        }
    }

    @GetMapping("/visionaMaterialePartecipanti/{contestId}")
    public ResponseEntity<?> visionaMaterialePartecipanti(@PathVariable Long contestId) {
        try {
            List<Partecipazione> partecipazioni = partecipazioneService.trovaPartecipazioniPerContestId(contestId);
            // Assumi di avere un metodo che converte Partecipazione in un DTO o in un'altra forma se necessario
            // Altrimenti restituisci direttamente le entità
            return ResponseEntity.ok(partecipazioni);
        } catch (Exception e) {
            // Assicurati che il messaggio di errore sia restituito come un oggetto, non come una stringa cruda
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nella visualizzazione del materiale dei partecipanti: " + e.getMessage());
        }
    }



    @PutMapping("/approvaPartecipazione/{partecipazioneId}")
    public ResponseEntity<String> approvaPartecipazione(@PathVariable Long partecipazioneId) {
        try {
            partecipazioneService.approvaPartecipazione(partecipazioneId);
            return ResponseEntity.ok("Partecipazione approvata con successo");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/mostra/{id}")
    public ResponseEntity<ContestDiContribuzione> mostraContestDiContribuzione(@PathVariable Long id) {
        return contestService.trovaContestDiContribuzionePerId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/cancella/{id}")
    public ResponseEntity<Void> cancellaContestDiContribuzione(@PathVariable Long id) {
        contestService.cancellaContestDiContribuzionePerId(id);
        return ResponseEntity.ok().build();
    }
}
