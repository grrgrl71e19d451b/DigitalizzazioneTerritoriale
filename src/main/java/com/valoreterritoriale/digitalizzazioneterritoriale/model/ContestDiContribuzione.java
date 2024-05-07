package com.valoreterritoriale.digitalizzazioneterritoriale.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * Modello per la rappresentazione di un contest di contribuzione.
 */
@Entity
@Table(name = "contest_di_contribuzione")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestDiContribuzione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String descrizione;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCreazione;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEvento;

    @ManyToOne
    @JoinColumn(name = "creatore_id", nullable = false)
    private Utente creatore;

}
