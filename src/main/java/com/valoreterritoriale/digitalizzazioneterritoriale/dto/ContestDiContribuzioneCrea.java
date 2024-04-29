package com.valoreterritoriale.digitalizzazioneterritoriale.dto;

import com.valoreterritoriale.digitalizzazioneterritoriale.model.Utente;
import lombok.Data;
import java.util.Date;

@Data
public class ContestDiContribuzioneCrea {
    private String nome;
    private String descrizione;
    private Date dataEvento;
    private Utente creatore;
}