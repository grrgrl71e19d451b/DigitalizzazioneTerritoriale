package com.valoreterritoriale.digitalizzazioneterritoriale.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "itinerario")
public class Itinerario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CREATORE_ID", referencedColumnName = "id")
    private Utente creatore;  // Assicurati che sia mappato a CREATORE_ID

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "itinerario_punto_di_interesse",
            joinColumns = @JoinColumn(name = "itinerario_id"),
            inverseJoinColumns = @JoinColumn(name = "punto_di_interesse_id")
    )
    private List<PuntoDiInteresse> puntiDiInteresse = new ArrayList<>();


    private String nomeItinerario;
    private String descrizione;
    private LocalDateTime dataPubblicazione;
    private boolean pending;

    // Metodo per aggiungere un punto di interesse
    public void addPuntoDiInteresse(PuntoDiInteresse puntoDiInteresse) {
        if (puntiDiInteresse == null) {
            puntiDiInteresse = new ArrayList<>();
        }
        puntiDiInteresse.add(puntoDiInteresse);
    }
}
