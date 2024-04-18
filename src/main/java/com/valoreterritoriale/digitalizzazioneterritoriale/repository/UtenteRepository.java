package com.valoreterritoriale.digitalizzazioneterritoriale.repository;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends CrudRepository<Utente, Integer> {
}
