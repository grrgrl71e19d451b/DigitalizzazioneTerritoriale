package com.valoreterritoriale.digitalizzazioneterritoriale.service;
import com.valoreterritoriale.digitalizzazioneterritoriale.dto.PuntoDiInteresseCrea;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.PuntoDiInteresse;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.PuntoDiInteresseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuntoDiInteresseService {

    private final PuntoDiInteresseRepository puntoDiInteresseRepository;

    @Autowired
    public PuntoDiInteresseService(PuntoDiInteresseRepository puntoDiInteresseRepository) {
        this.puntoDiInteresseRepository = puntoDiInteresseRepository;
    }

    public boolean createPuntoDiInteresse(PuntoDiInteresseCrea puntoDiInteresseDTO) {
        try {
            PuntoDiInteresse puntoDiInteresse = new PuntoDiInteresse();
            puntoDiInteresse.setNomePuntoDiInteresse(puntoDiInteresseDTO.getNomePuntoDiInteresse());  // Aggiunta del nuovo campo
            puntoDiInteresse.setDescrizione(puntoDiInteresseDTO.getDescrizione());
            puntoDiInteresse.setDataPubblicazione(puntoDiInteresseDTO.getDataPubblicazione());
            puntoDiInteresse.setPending(puntoDiInteresseDTO.isPending());
            puntoDiInteresse.setLatitudine(puntoDiInteresseDTO.getLatitudine());
            puntoDiInteresse.setLongitudine(puntoDiInteresseDTO.getLongitudine());
            puntoDiInteresseRepository.save(puntoDiInteresse);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public boolean deletePuntoDiInteresse(Long id) {
        if (puntoDiInteresseRepository.existsById(id)) {
            puntoDiInteresseRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public PuntoDiInteresse findPuntoDiInteresseById(Long id) {
        return puntoDiInteresseRepository.findById(id).orElse(null);
    }

    public List<PuntoDiInteresse> findAllPendingPuntiDiInteresse() {
        return puntoDiInteresseRepository.findByPending(true);
    }

    public void approvePuntoDiInteresse(Long puntoDiInteresseId) {
        PuntoDiInteresse puntoDiInteresse = findPuntoDiInteresseById(puntoDiInteresseId);
        if (puntoDiInteresse != null) {
            puntoDiInteresse.setPending(false);
            puntoDiInteresseRepository.save(puntoDiInteresse);
        } else {
            throw new RuntimeException("Impossibile trovare il punto di interesse con ID: " + puntoDiInteresseId);
        }
    }

}
