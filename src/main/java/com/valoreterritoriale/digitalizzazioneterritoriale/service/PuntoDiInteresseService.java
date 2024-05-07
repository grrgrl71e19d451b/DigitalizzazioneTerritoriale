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

/**
 * Servizio per la gestione dei punti di interesse.
 */
@Service
public class PuntoDiInteresseService {

    private final PuntoDiInteresseRepository puntoDiInteresseRepository;
    private final UtenteRepository utenteRepository;  // Assicurati che questo repository sia iniettato

    /**
     * Costruttore per iniezione delle dipendenze necessarie per la gestione dei punti di interesse.
     *
     * @param puntoDiInteresseRepository il repository per la gestione dei dati dei punti di interesse
     * @param utenteRepository il repository per la gestione degli utenti
     */
    @Autowired
    public PuntoDiInteresseService(PuntoDiInteresseRepository puntoDiInteresseRepository, UtenteRepository utenteRepository) {
        this.puntoDiInteresseRepository = puntoDiInteresseRepository;
        this.utenteRepository = utenteRepository;  // Iniezione tramite costruttore
    }

    /**
     * Crea un nuovo punto di interesse nel database.
     *
     * @param puntoDiInteresseDTO il DTO che contiene i dati del nuovo punto di interesse
     * @return true se il punto di interesse è stato creato con successo, false altrimenti
     */
    public boolean creaPuntoDiInteresse(PuntoDiInteresseCrea puntoDiInteresseDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Utente creatore = utenteRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

            PuntoDiInteresse puntoDiInteresse = createPuntoDiInteresse(puntoDiInteresseDTO, creatore);
            puntoDiInteresseRepository.save(puntoDiInteresse);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Metodo helper per creare un'entità PuntoDiInteresse a partire da un DTO.
     *
     * @param puntoDiInteresseDTO il DTO con i dati del punto di interesse
     * @param creatore l'utente che ha creato il punto di interesse
     * @return un'istanza di PuntoDiInteresse popolata con i dati dal DTO
     */
    private static PuntoDiInteresse createPuntoDiInteresse(PuntoDiInteresseCrea puntoDiInteresseDTO, Utente creatore) {
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

    /**
     * Cancella un punto di interesse dal database.
     *
     * @param id l'identificativo del punto di interesse da cancellare
     * @return true se il punto di interesse è stato cancellato con successo, false altrimenti
     */
    public boolean cancellaPuntoDiInteresse(Long id) {
        if (puntoDiInteresseRepository.existsById(id)) {
            puntoDiInteresseRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Recupera un punto di interesse dal database tramite il suo ID.
     *
     * @param id l'ID del punto di interesse da recuperare
     * @return l'entità PuntoDiInteresse se trovata, altrimenti null
     */
    public PuntoDiInteresse visualizzaPuntoDiInteresseById(Long id) {
        return puntoDiInteresseRepository.findById(id).orElse(null);
    }

    /**
     * Restituisce una lista di tutti i punti di interesse in attesa di approvazione.
     *
     * @return una lista di PuntoDiInteresse
     */
    public List<PuntoDiInteresse> visualizzaPendingPuntiDiInteresse() {
        return puntoDiInteresseRepository.findByPending(true);
    }

    /**
     * Approva un punto di interesse, cambiando il suo stato da "in attesa" a "approvato".
     *
     * @param puntoDiInteresseId l'ID del punto di interesse da approvare
     */
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
