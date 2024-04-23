package com.valoreterritoriale.digitalizzazioneterritoriale.repository;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Preferito;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import com.valoreterritoriale.digitalizzazioneterritoriale.model.Itinerario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PreferitiRepository extends JpaRepository<Preferito, Long> {
    Optional<Preferito> findByUtenteAndItinerario(Utente utente, Itinerario itinerario);

    List<Preferito> findByUtente(Utente utente);
}