package com.valoreterritoriale.digitalizzazioneterritoriale.dto;

import lombok.Data;

/**
 * DTO per l'aggiunta di un punto di interesse a un itinerario.
 */
@Data
public class PoiAItinerarioAggiungi {
    private int idItinerario;
    private int idPuntoDiInteresse;
}
