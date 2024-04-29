package com.valoreterritoriale.digitalizzazioneterritoriale.dto;

import lombok.Data;

@Data
public class UtenteCrea {
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
