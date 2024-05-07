package com.valoreterritoriale.digitalizzazioneterritoriale.dto;

import lombok.Data;

/**
 * DTO per la rimozione di un itinerario dai preferiti di un utente.
 */
@Data
public class PreferitoRimuovi {
    private Long utenteId;
    private Long itinerarioId;
}
