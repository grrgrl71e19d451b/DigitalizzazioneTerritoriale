package com.valoreterritoriale.digitalizzazioneterritoriale.service;

import com.valoreterritoriale.digitalizzazioneterritoriale.dto.ContestDiContribuzioneCrea;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.ContestDiContribuzione;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.ContestDiContribuzioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ContestDiContribuzioneService {
    private final ContestDiContribuzioneRepository contestRepository;

    @Autowired
    public ContestDiContribuzioneService(ContestDiContribuzioneRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

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

    public Optional<ContestDiContribuzione> trovaContestDiContribuzionePerId(Long id) {
        return contestRepository.findById(id);
    }


    public void cancellaContestDiContribuzionePerId(Long id) {
        contestRepository.deleteById(id);
    }
}
