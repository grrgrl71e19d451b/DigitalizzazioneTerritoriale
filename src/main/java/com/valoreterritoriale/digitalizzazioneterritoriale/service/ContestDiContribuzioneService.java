package com.valoreterritoriale.digitalizzazioneterritoriale.service;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.ContestDiContribuzioneCrea;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.ContestDiContribuzione;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.ContestDiContribuzioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * Servizio per la gestione dei contest di contribuzione.
 */
@Service
public class ContestDiContribuzioneService {
    private final ContestDiContribuzioneRepository contestRepository;

    /**
     * Costruttore del servizio ContestDiContribuzioneService.
     * @param contestRepository Il repository dei contest di contribuzione.
     */
    @Autowired
    public ContestDiContribuzioneService(ContestDiContribuzioneRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    /**
     * Metodo per creare un nuovo contest di contribuzione.
     * @param contestDiContribuzioneCrea I dati del contest di contribuzione da creare.
     * @return true se il contest è stato creato con successo, altrimenti false.
     */
    public boolean creaContestDiContribuzione(ContestDiContribuzioneCrea contestDiContribuzioneCrea) {
        try {
            ContestDiContribuzione contest = new ContestDiContribuzione();
            contest.setNome(contestDiContribuzioneCrea.getNome());
            contest.setDescrizione(contestDiContribuzioneCrea.getDescrizione());
            contest.setDataCreazione(new Date());
            contest.setDataEvento(contestDiContribuzioneCrea.getDataEvento());
            contest.setCreatore(contestDiContribuzioneCrea.getCreatore());

            contestRepository.save(contest);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Trova un contest di contribuzione per l'ID specificato.
     * @param id L'ID del contest di contribuzione da trovare.
     * @return Il contest di contribuzione trovato, se presente.
     */
    public Optional<ContestDiContribuzione> trovaContestDiContribuzionePerId(Long id) {
        return contestRepository.findById(id);
    }

    /**
     * Cancella un contest di contribuzione per l'ID specificato.
     * @param id L'ID del contest di contribuzione da cancellare.
     */
    public void cancellaContestDiContribuzionePerId(Long id) {
        contestRepository.deleteById(id);
    }
}
