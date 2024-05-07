package com.valoreterritoriale.digitalizzazioneterritoriale.dto;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import lombok.Data;
import java.util.Date;

/**
 * DTO per la creazione di un contest di contribuzione.
 */
@Data
public class ContestDiContribuzioneCrea {
    private String nome;
    private String descrizione;
    private Date dataEvento;
    private Utente creatore;
}
