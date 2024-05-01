package com.valoreterritoriale.digitalizzazioneterritoriale.service;
import com.valoreterritoriale.digitalizzazioneterritoriale.dto.PuntoDiInteresseCrea;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.PuntoDiInteresse;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.PuntoDiInteresseRepository;
import com.valoreterritoriale.digitalizzazioneterritoriale.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuntoDiInteresseService {

    private final PuntoDiInteresseRepository puntoDiInteresseRepository;
    private final UtenteRepository utenteRepository;  // Assicurati che questo repository sia iniettato

    @Autowired
    public PuntoDiInteresseService(PuntoDiInteresseRepository puntoDiInteresseRepository, UtenteRepository utenteRepository) {
        this.puntoDiInteresseRepository = puntoDiInteresseRepository;
        this.utenteRepository = utenteRepository;  // Iniezione tramite costruttore
    }

    public boolean creaPuntoDiInteresse(PuntoDiInteresseCrea puntoDiInteresseDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Utente creatore = utenteRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

            PuntoDiInteresse puntoDiInteresse = getPuntoDiInteresse(puntoDiInteresseDTO, creatore);
            puntoDiInteresseRepository.save(puntoDiInteresse);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static PuntoDiInteresse getPuntoDiInteresse(PuntoDiInteresseCrea puntoDiInteresseDTO, Utente creatore) {
        PuntoDiInteresse puntoDiInteresse = new PuntoDiInteresse();
        puntoDiInteresse.setNomePuntoDiInteresse(puntoDiInteresseDTO.getNomePuntoDiInteresse());
        puntoDiInteresse.setDescrizione(puntoDiInteresseDTO.getDescrizione());
        puntoDiInteresse.setDataPubblicazione(puntoDiInteresseDTO.getDataPubblicazione());
        puntoDiInteresse.setPending(puntoDiInteresseDTO.isPending());
        puntoDiInteresse.setLatitudine(puntoDiInteresseDTO.getLatitudine());
        puntoDiInteresse.setLongitudine(puntoDiInteresseDTO.getLongitudine());
        puntoDiInteresse.setCreatore(creatore);  // Imposta il creatore
        return puntoDiInteresse;
    }

    public boolean cancellaPuntoDiInteresse(Long id) {
        if (puntoDiInteresseRepository.existsById(id)) {
            puntoDiInteresseRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public PuntoDiInteresse visualizzaPuntoDiInteresseById(Long id) {
        return puntoDiInteresseRepository.findById(id).orElse(null);
    }

    public List<PuntoDiInteresse> visualizzaPendingPuntiDiInteresse() {
        return puntoDiInteresseRepository.findByPending(true);
    }

    public void approvaPuntoDiInteresse(Long puntoDiInteresseId) {
        PuntoDiInteresse puntoDiInteresse = visualizzaPuntoDiInteresseById(puntoDiInteresseId);
        if (puntoDiInteresse != null) {
            puntoDiInteresse.setPending(false);
            puntoDiInteresseRepository.save(puntoDiInteresse);
        } else {
            throw new RuntimeException("Impossibile trovare il punto di interesse con ID: " + puntoDiInteresseId);
        }
    }


}
