package com.valoreterritoriale.digitalizzazioneterritoriale.dto;

import lombok.Data;

@Data  // Questa annotazione di Lombok genera automaticamente getter e setter per tutti i campi
public class UtenteCrea {
    private String nome;
    private String cognome;
    private String email;
    private String comune;
    private String ruolo;
    private int eta;
}
