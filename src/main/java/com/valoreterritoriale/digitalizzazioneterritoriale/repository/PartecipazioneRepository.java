package com.valoreterritoriale.digitalizzazioneterritoriale.repository;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Partecipazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartecipazioneRepository extends JpaRepository<Partecipazione, Long> {

    /**
     * Verifica se esiste una partecipazione con il codice specificato e l'ID del contest specificato.
     *
     * @param codicePartecipazione Il codice della partecipazione da controllare.
     * @param contestId L'ID del contest associato alla partecipazione.
     * @return True se esiste una partecipazione con il codice specificato e l'ID del contest specificato, altrimenti false.
     */
    boolean existsByCodicePartecipazioneAndContestId(String codicePartecipazione, Long contestId);

    /**
     * Trova tutte le partecipazioni associate a un contest specificato.
     *
     * @param contestId L'ID del contest associato alle partecipazioni da trovare.
     * @return Una lista di partecipazioni associate al contest specificato.
     */
    List<Partecipazione> findByContestId(Long contestId);
}
