package com.valoreterritoriale.digitalizzazioneterritoriale.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modello per la rappresentazione di un preferito, che associa un utente a un itinerario preferito.
 */
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

    /**
     * Costruttore per creare un nuovo preferito associando un utente e un itinerario.
     *
     * @param utente     L'utente associato al preferito.
     * @param itinerario L'itinerario associato al preferito.
     */
    public Preferito(Utente utente, Itinerario itinerario) {
        this.utente = utente;
        this.itinerario = itinerario;
    }
}
