package com.valoreterritoriale.digitalizzazioneterritoriale.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@Table(name = "punto_di_interesse")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creatore_id")
    private Utente creatore;  // Collegamento a Utente
}