package com.valoreterritoriale.digitalizzazioneterritoriale.repository;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.ContestDiContribuzione;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository per la gestione dei contest di contribuzione.
 */
public interface ContestDiContribuzioneRepository extends JpaRepository<ContestDiContribuzione, Long> {
}
