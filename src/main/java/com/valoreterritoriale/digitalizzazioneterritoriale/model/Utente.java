package com.valoreterritoriale.digitalizzazioneterritoriale.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "utente")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private String cognome;
    private String email;
    private String comune;
    private String ruolo;
    private int eta;
}
