package com.valoreterritoriale.digitalizzazioneterritoriale.repository;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Preferito;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Itinerario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository per la gestione dei preferiti degli utenti.
 */
@Repository
public interface PreferitiRepository extends JpaRepository<Preferito, Long> {

    /**
     * Trova un preferito associato all'utente e all'itinerario specificati.
     *
     * @param utente L'utente associato al preferito.
     * @param itinerario L'itinerario associato al preferito.
     * @return Un Optional contenente il preferito trovato, o vuoto se non trovato.
     */
    Optional<Preferito> findByUtenteAndItinerario(Utente utente, Itinerario itinerario);

    /**
     * Trova tutti i preferiti associati all'utente specificato.
     *
     * @param utente L'utente associato ai preferiti.
     * @return Una lista di preferiti dell'utente.
     */
    List<Preferito> findByUtente(Utente utente);
}
