package com.valoreterritoriale.digitalizzazioneterritoriale.repository;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Partecipazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartecipazioneRepository extends JpaRepository<Partecipazione, Long> {
    boolean existsByCodicePartecipazioneAndContestId(String codicePartecipazione, Long contestId);

    List<Partecipazione> findByContestId(Long contestId);
}