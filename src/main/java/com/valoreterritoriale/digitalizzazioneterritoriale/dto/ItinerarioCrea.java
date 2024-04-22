package com.valoreterritoriale.digitalizzazioneterritoriale.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ItinerarioCrea {
    private Long id;
    private Long idUtente; // Sarà utilizzato per associare l'itinerario all'utente creatore
    private String nomeItinerario;
    private String descrizione;
    private LocalDateTime dataPubblicazione;
    private boolean pending;
}
