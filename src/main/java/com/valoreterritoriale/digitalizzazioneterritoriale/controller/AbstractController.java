package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Classe astratta che definisce lo scheletro di un controller e i metodi template per le operazioni CRUD.
 * Implementa il pattern Template Method per consentire alle sottoclassi di definire i dettagli
 * specifici delle operazioni.
 */
@Component
public abstract class AbstractController {

    /**
     * Metodo template per gestire la creazione di una risorsa.
     * @param request Oggetto della richiesta.
     * @param authentication Informazioni sull'autenticazione dell'utente.
     * @param <T> Tipo di oggetto della risposta.
     * @return ResponseEntity che incapsula l'esito dell'operazione di creazione.
     */
    protected abstract <T> ResponseEntity<T> create(Object request, Authentication authentication);

    /**
     * Metodo template per gestire il prelievo di una risorsa.
     * @param id Identificativo della risorsa da prelevare.
     * @param authentication Informazioni sull'autenticazione dell'utente.
     * @param <T> Tipo di oggetto della risposta.
     * @return ResponseEntity che incapsula l'oggetto prelevato o un messaggio di errore.
     */
    protected abstract <T> ResponseEntity<T> read(Long id, Authentication authentication);

    /**
     * Metodo template per gestire l'aggiornamento di una risorsa.
     * @param id Identificativo della risorsa da aggiornare.
     * @param request Oggetto della richiesta contenente i dati aggiornati.
     * @param authentication Informazioni sull'autenticazione dell'utente.
     * @param <T> Tipo di oggetto della risposta.
     * @return ResponseEntity che incapsula l'esito dell'operazione di aggiornamento.
     */
    protected abstract <T> ResponseEntity<T> update(Long id, Object request, Authentication authentication);

    /**
     * Metodo template per gestire la cancellazione di una risorsa.
     * @param id Identificativo della risorsa da cancellare.
     * @param authentication Informazioni sull'autenticazione dell'utente.
     * @param <T> Tipo di oggetto della risposta.
     * @return ResponseEntity che incapsula l'esito dell'operazione di cancellazione.
     */
    protected abstract <T> ResponseEntity<T> delete(Long id, Authentication authentication);

    /**
     * Crea una ResponseEntity con il corpo e lo status specificati.
     * @param body Corpo della risposta.
     * @param status Status della risposta.
     * @return ResponseEntity con il corpo e lo status specificati.
     */
    protected ResponseEntity<String> createResponse(String body, HttpStatus status) {
        return new ResponseEntity<>(body, status);
    }

    /**
     * Crea una ResponseEntity di successo con il corpo specificato.
     * @param body Corpo della risposta.
     * @return ResponseEntity di successo con il corpo specificato.
     */
    protected ResponseEntity<String> createSuccessResponse(String body) {
        return createResponse(body, HttpStatus.OK);
    }

    /**
     * Crea una ResponseEntity di errore con il messaggio e lo status specificati.
     * @param message Messaggio di errore.
     * @param status Status della risposta.
     * @return ResponseEntity di errore con il messaggio e lo status specificati.
     */
    protected ResponseEntity<String> createErrorResponse(String message, HttpStatus status) {
        return createResponse(message, status);
    }

    /**
     * Crea una ResponseEntity contenente una lista di oggetti.
     * @param body Lista di oggetti.
     * @param <T> Tipo degli oggetti nella lista.
     * @return ResponseEntity contenente la lista di oggetti.
     */
    protected <T> ResponseEntity<List<T>> createListResponse(List<T> body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * Crea una ResponseEntity contenente un singolo oggetto.
     * @param object Oggetto da includere nella risposta.
     * @param <T> Tipo dell'oggetto.
     * @return ResponseEntity contenente l'oggetto.
     */
    protected <T> ResponseEntity<T> createObjectResponse(T object) {
        return ResponseEntity.ok(object);
    }
}
