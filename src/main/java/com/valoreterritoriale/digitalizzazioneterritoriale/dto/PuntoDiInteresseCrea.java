package com.valoreterritoriale.digitalizzazioneterritoriale.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PuntoDiInteresseCrea {
    private Long id;
    private String descrizione;
    private LocalDateTime dataPubblicazione;
    private boolean pending;
}
