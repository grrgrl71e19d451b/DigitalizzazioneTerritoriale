package com.valoreterritoriale.digitalizzazioneterritoriale.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Modello per la rappresentazione di un utente.
 */
@Entity
@Table(name = "utente")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String comune;
    private String ruolo;
    private int eta;
    private String username;
    private String password;
}
