package com.valoreterritoriale.digitalizzazioneterritoriale.repository;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Itinerario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository per la gestione degli itinerari.
 */
@Repository
public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {

    /**
     * Trova tutti gli itinerari pendenti (in attesa di approvazione).
     *
     * @return Una lista di itinerari pendenti.
     */
    List<Itinerario> findByPendingTrue();
}
