package com.valoreterritoriale.digitalizzazioneterritoriale.repository;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.PuntoDiInteresse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PuntoDiInteresseRepository extends JpaRepository<PuntoDiInteresse, Long> {
    List<PuntoDiInteresse> findByPending(boolean pending);
}
