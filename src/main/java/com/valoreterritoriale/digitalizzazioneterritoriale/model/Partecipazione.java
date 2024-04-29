package com.valoreterritoriale.digitalizzazioneterritoriale.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
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

    public Partecipazione(Utente utente, ContestDiContribuzione contest, String codicePartecipazione, byte[] file) {
        this.utente = utente;
        this.contest = contest;
        this.codicePartecipazione = codicePartecipazione;
        this.file = file;
        this.isValidated = false;
    }
}
