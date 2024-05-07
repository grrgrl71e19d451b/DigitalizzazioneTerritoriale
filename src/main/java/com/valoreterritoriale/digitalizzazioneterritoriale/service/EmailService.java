package com.valoreterritoriale.digitalizzazioneterritoriale.service;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.RichiestaModificaRuolo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servizio per la gestione dell'invio di email.
 */
@Service
public class EmailService {
    private final JavaMailSender mailSender;

    /**
     * Costruttore del servizio di email.
     *
     * @param mailSender Il componente per l'invio delle email.
     */
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Invia una email di notifica per la modifica di ruolo.
     *
     * @param username Nome dell'utente che richiede la modifica.
     * @param richiestaModificaRuolo Dettagli della richiesta di modifica.
     */
    public void sendRuoloModificaEmail(String username, RichiestaModificaRuolo richiestaModificaRuolo) {
        SimpleMailMessage mailMessage = createMailMessageForRuoloModifica(username, richiestaModificaRuolo);
        mailSender.send(mailMessage);
    }

    /**
     * Invia una email di invito a partecipare a un contest.
     *
     * @param name Nome del destinatario.
     * @param email Email del destinatario.
     * @param contestId ID del contest a cui è invitato.
     * @param codicePartecipazione Codice per la partecipazione al contest.
     */
    public void sendContestPartecipationEmail(String name, String email, Long contestId, String codicePartecipazione) {
        SimpleMailMessage mailMessage = createMailMessageForContestPartecipation(name, email, contestId, codicePartecipazione);
        mailSender.send(mailMessage);
    }

    /**
     * Crea un messaggio di posta per la modifica del ruolo.
     *
     * @param username Nome dell'utente.
     * @param richiestaModificaRuolo Dettagli della richiesta.
     * @return Il messaggio di posta configurato.
     */
    private SimpleMailMessage createMailMessageForRuoloModifica(String username, RichiestaModificaRuolo richiestaModificaRuolo) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(richiestaModificaRuolo.getEmailGestorePiattaforma());
        mailMessage.setSubject("Richiesta di Modifica Ruolo");
        mailMessage.setText("L'utente " + username + " ha richiesto di cambiare il suo ruolo in " + richiestaModificaRuolo.getNuovoRuolo() +
                ". Motivazione: " + richiestaModificaRuolo.getMotivazione());
        return mailMessage;
    }

    /**
     * Crea un messaggio di posta per l'invito a un contest.
     *
     * @param name Nome del destinatario.
     * @param email Email del destinatario.
     * @param contestId ID del contest.
     * @param codicePartecipazione Codice per la partecipazione.
     * @return Il messaggio di posta configurato.
     */
    private SimpleMailMessage createMailMessageForContestPartecipation(String name, String email, Long contestId, String codicePartecipazione) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Invito a partecipare al Contest");
        mailMessage.setText("Ciao " + name + ",\n\nSei stato invitato a partecipare al contest con ID: " + contestId + ". Utilizza questo codice di partecipazione: " + codicePartecipazione + "\n\nGrazie per partecipare!");
        return mailMessage;
    }

}
