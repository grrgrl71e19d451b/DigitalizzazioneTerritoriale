package com.valoreterritoriale.digitalizzazioneterritoriale.service;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Partecipazione;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.ContestDiContribuzione;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.PartecipazioneRepository;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.UtenteRepository;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.ContestDiContribuzioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servizio per la gestione delle partecipazioni agli eventi di contribuzione.
 */
@Service
public class PartecipazioneService {

    private final PartecipazioneRepository partecipazioneRepository;
    private final UtenteRepository utenteRepository;
    private final ContestDiContribuzioneRepository contestRepository;

    /**
     * Costruttore per iniezione delle dipendenze dei repository necessari.
     *
     * @param partecipazioneRepository Repository per gestire le partecipazioni.
     * @param utenteRepository Repository per gestire gli utenti.
     * @param contestRepository Repository per gestire i contest di contribuzione.
     */
    @Autowired
    public PartecipazioneService(PartecipazioneRepository partecipazioneRepository,
                                 UtenteRepository utenteRepository,
                                 ContestDiContribuzioneRepository contestRepository) {
        this.partecipazioneRepository = partecipazioneRepository;
        this.utenteRepository = utenteRepository;
        this.contestRepository = contestRepository;
    }

    /**
     * Verifica la validità di un codice di partecipazione per un contest.
     *
     * @param codicePartecipazione Il codice da verificare.
     * @param contestId L'identificativo del contest.
     * @return true se il codice è valido o se è il codice universale, altrimenti false.
     */
    public boolean verificaCodice(String codicePartecipazione, Long contestId) {
        boolean esistePartecipazione = partecipazioneRepository.existsByCodicePartecipazioneAndContestId(codicePartecipazione, contestId);
        boolean isCodeUniversale = "XYZ123".equals(codicePartecipazione);
        return esistePartecipazione || isCodeUniversale;
    }

    /**
     * Aggiunge un utente come partecipante a un contest.
     *
     * @param userId Identificativo dell'utente.
     * @param contestId Identificativo del contest.
     * @param codicePartecipazione Codice di partecipazione usato dall'utente.
     * @param file Il file di contribuzione presentato dall'utente.
     */
    public void aggiungiPartecipanteAlContest(Long userId, Long contestId, String codicePartecipazione, byte[] file) {
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + userId));
        ContestDiContribuzione contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new IllegalArgumentException("Contest non trovato con ID: " + contestId));

        Partecipazione partecipazione = new Partecipazione(utente, contest, codicePartecipazione, file);
        partecipazioneRepository.save(partecipazione);
    }

    /**
     * Ritorna tutte le partecipazioni relative a un contest specifico.
     *
     * @param contestId Identificativo del contest.
     * @return Lista di partecipazioni al contest indicato.
     */
    public List<Partecipazione> trovaPartecipazioniPerContestId(Long contestId) {
        return partecipazioneRepository.findByContestId(contestId);
    }

    /**
     * Approva una partecipazione, marcandola come validata.
     *
     * @param partecipazioneId Identificativo della partecipazione da approvare.
     */
    public void approvaPartecipazione(Long partecipazioneId) {
        Partecipazione partecipazione = partecipazioneRepository.findById(partecipazioneId)
                .orElseThrow(() -> new RuntimeException("Partecipazione non trovata con ID: " + partecipazioneId));

        if (!partecipazione.getIsValidated()) {
            partecipazione.setIsValidated(true);
            partecipazioneRepository.save(partecipazione);
        } else {
            throw new RuntimeException("La partecipazione è già stata approvata.");
        }
    }
}
