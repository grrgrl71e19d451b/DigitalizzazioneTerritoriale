package com.valoreterritoriale.digitalizzazioneterritoriale.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Modello per la rappresentazione di una partecipazione.
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Partecipazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private Utente utente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contestId", referencedColumnName = "id")
    private ContestDiContribuzione contest;

    private String codicePartecipazione;
    private Boolean isValidated;
    @Lob
    private byte[] file;

    /**
     * Costruttore per la creazione di una partecipazione.
     *
     * @param utente                L'utente che partecipa al contest.
     * @param contest               Il contest di contribuzione a cui partecipa l'utente.
     * @param codicePartecipazione  Il codice univoco associato alla partecipazione.
     * @param file                  Il file associato alla partecipazione.
     */
    public Partecipazione(Utente utente, ContestDiContribuzione contest, String codicePartecipazione, byte[] file) {
        this.utente = utente;
        this.contest = contest;
        this.codicePartecipazione = codicePartecipazione;
        this.file = file;
        this.isValidated = false;
    }
}
