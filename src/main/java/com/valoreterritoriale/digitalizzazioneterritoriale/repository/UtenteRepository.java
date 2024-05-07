package com.valoreterritoriale.digitalizzazioneterritoriale.repository;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository per la gestione degli utenti.
 */
@Repository
public interface UtenteRepository extends CrudRepository<Utente, Long> {

    /**
     * Trova un utente in base al nome utente specificato.
     *
     * @param username Il nome utente da cercare.
     * @return Un Optional contenente l'utente trovato, se presente.
     */
    Optional<Utente> findByUsername(String username);
}
