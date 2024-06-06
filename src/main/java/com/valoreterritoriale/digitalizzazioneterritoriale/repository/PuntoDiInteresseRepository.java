package com.valoreterritoriale.digitalizzazioneterritoriale.repository;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.PuntoDiInteresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository per la gestione dei punti di interesse.
 */
@Repository
public interface PuntoDiInteresseRepository extends JpaRepository<PuntoDiInteresse, Long> {

    /**
     * Trova tutti i punti di interesse in base allo stato di pending specificato.
     *
     * @param pending Lo stato di pending da cercare.
     * @return Una lista di punti di interesse con lo stato specificato.
     */
    List<PuntoDiInteresse> findByPending(boolean pending);
}
