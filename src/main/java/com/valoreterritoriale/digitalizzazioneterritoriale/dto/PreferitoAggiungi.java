package com.valoreterritoriale.digitalizzazioneterritoriale.dto;

import lombok.Data;

/**
 * DTO per l'aggiunta di un itinerario ai preferiti di un utente.
 */
@Data
public class PreferitoAggiungi {
    private Long utenteId;
    private Long itinerarioId;
}
