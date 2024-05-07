package com.valoreterritoriale.digitalizzazioneterritoriale.dto;

import lombok.Data;

/**
 * DTO per l'invito di un utente a un contest.
 */
@Data
public class ContestUtenteInvita {
    private String email;
    private String name;
}
