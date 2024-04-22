package com.valoreterritoriale.digitalizzazioneterritoriale.repository;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Itinerario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {

    // Metodo per trovare itinerari pendenti (in attesa di approvazione)
    List<Itinerario> findByPendingTrue();
}
