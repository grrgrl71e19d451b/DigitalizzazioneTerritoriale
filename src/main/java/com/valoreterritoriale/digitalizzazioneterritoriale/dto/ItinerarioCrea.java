package com.valoreterritoriale.digitalizzazioneterritoriale.dto;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ItinerarioCrea {
    private Long id;
    private Utente creatore;
    private String nomeItinerario;
    private String descrizione;
    private LocalDateTime dataPubblicazione;
    private boolean pending;
}