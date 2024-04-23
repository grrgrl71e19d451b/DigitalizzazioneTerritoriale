package com.valoreterritoriale.digitalizzazioneterritoriale.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Preferito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "itinerario_id", nullable = false)
    private Itinerario itinerario;

    public Preferito(Utente utente, Itinerario itinerario) {
        this.utente = utente;
        this.itinerario = itinerario;
    }
}