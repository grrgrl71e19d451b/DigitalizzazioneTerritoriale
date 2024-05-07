/**
 * Classe principale dell'applicazione per la digitalizzazione territoriale.
 * Questa classe avvia l'applicazione Spring Boot.
 */
package com.valoreterritoriale.digitalizzazioneterritoriale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DigitalizzazioneTerritorialeApplication {

	/**
	 * Metodo main per avviare l'applicazione.
	 * @param args Argomenti della riga di comando
	 */
	public static void main(String[] args) {
		SpringApplication.run(DigitalizzazioneTerritorialeApplication.class, args);
	}

}
