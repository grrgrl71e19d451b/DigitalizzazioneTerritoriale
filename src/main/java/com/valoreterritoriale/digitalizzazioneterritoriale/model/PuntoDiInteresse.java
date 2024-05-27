package com.valoreterritoriale.digitalizzazioneterritoriale.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Modello per la rappresentazione di un punto di interesse.
 */
@Entity
@Table(name = "punto_di_interesse")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PuntoDiInteresse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomePuntoDiInteresse;
    private String descrizione;
    private LocalDateTime dataPubblicazione;
    private boolean pending;
    private double latitudine;
    private double longitudine;

    /**
     * Il creatore del punto di interesse.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creatore_id")
    private Utente creatore;  // Collegamento a Utente
}
