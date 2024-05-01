package com.valoreterritoriale.digitalizzazioneterritoriale.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configurazione dell'applicazione Spring.
 */
@Configuration
public class AppConfig {
    /**
     * Crea un'istanza di RestTemplate, utilizzata per effettuare chiamate REST.
     * RestTemplate può essere utilizzato per interagire con servizi API di OpenStreetMap (OSM)
     * e recuperare dati geografici e informazioni sul territorio.
     *
     * @return un'istanza di RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}