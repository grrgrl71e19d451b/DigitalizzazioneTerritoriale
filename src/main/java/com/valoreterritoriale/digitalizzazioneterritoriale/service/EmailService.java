package com.valoreterritoriale.digitalizzazioneterritoriale.service;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.RichiestaModificaRuolo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRuoloModificaEmail(String username, RichiestaModificaRuolo richiestaModificaRuolo) {
        SimpleMailMessage mailMessage = createMailMessageForRuoloModifica(username, richiestaModificaRuolo);
        mailSender.send(mailMessage);
    }

    public void sendContestPartecipationEmail(String username, Long contestId, String codicePartecipazione) {
        SimpleMailMessage mailMessage = createMailMessageForContestPartecipation(username, contestId, codicePartecipazione);
        mailSender.send(mailMessage);
    }

    private SimpleMailMessage createMailMessageForRuoloModifica(String username, RichiestaModificaRuolo richiestaModificaRuolo) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(richiestaModificaRuolo.getEmailGestorePiattaforma());
        mailMessage.setSubject("Richiesta di Modifica Ruolo");
        mailMessage.setText("L'utente " + username + " ha richiesto di cambiare il suo ruolo in " + richiestaModificaRuolo.getNuovoRuolo() +
                ". Motivazione: " + richiestaModificaRuolo.getMotivazione());
        return mailMessage;
    }

    private SimpleMailMessage createMailMessageForContestPartecipation(String username, Long contestId, String codicePartecipazione) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo("contest@piattaforma.com"); // Sostituire con l'email reale del responsabile dei contest
        mailMessage.setSubject("Nuova Partecipazione al Contest");
        mailMessage.setText("L'utente " + username + " ha partecipato al contest con ID " + contestId + " e codice di partecipazione " + codicePartecipazione);
        return mailMessage;
    }
}