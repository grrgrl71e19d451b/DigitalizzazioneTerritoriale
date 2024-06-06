package com.valoreterritoriale.digitalizzazioneterritoriale.repository;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.ContestDiContribuzione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository per la gestione dei contest di contribuzione.
 */
@Repository
public interface ContestDiContribuzioneRepository extends JpaRepository<ContestDiContribuzione, Long> {
}
