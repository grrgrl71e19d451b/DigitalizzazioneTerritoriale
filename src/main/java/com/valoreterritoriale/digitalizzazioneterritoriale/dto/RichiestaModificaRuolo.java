package com.valoreterritoriale.digitalizzazioneterritoriale.dto;

import lombok.Data;

/**
 * DTO per la richiesta di modifica del ruolo di un utente.
 */
@Data
public class RichiestaModificaRuolo {
    private String nuovoRuolo;
    private String motivazione;
    private String emailGestorePiattaforma;
}

