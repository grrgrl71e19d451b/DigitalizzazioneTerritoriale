package com.valoreterritoriale.digitalizzazioneterritoriale.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller per la gestione della homepage.
 */
@Controller
@RequestMapping("/home")
public class HomeController extends AbstractController {

    /**
     * Metodo per ottenere la homepage.
     *
     * @return ResponseEntity con la stringa di benvenuto nella homepage.
     */
    @GetMapping
    public ResponseEntity<String> getHomePage() {
        return createSuccessResponse("Welcome to the Home Page");
    }
}