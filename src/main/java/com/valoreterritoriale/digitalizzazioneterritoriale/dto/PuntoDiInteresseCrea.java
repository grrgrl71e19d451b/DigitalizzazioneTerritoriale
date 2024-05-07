package com.valoreterritoriale.digitalizzazioneterritoriale.dto;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO per la creazione di un punto di interesse.
 */
@Data
public class PuntoDiInteresseCrea {
    private Long id;
    private String nomePuntoDiInteresse;
    private String descrizione;
    private LocalDateTime dataPubblicazione;
    private boolean pending;
    private double latitudine;
    private double longitudine;
    private Utente creatore;
}
