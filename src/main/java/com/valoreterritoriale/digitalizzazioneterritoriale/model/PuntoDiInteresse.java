package com.valoreterritoriale.digitalizzazioneterritoriale.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "punto_di_interesse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuntoDiInteresse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descrizione;
    private LocalDateTime dataPubblicazione;
    private boolean pending;
}
