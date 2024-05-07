# Digitalizzazione Territoriale

**Autore:** Gabriele Guerrieri  
**Anno Accademico:** 2023-2024  
**Corso:** Informatica

## Descrizione
Questo progetto mira a potenziare e promuovere l'engagement della comunità locale attraverso una piattaforma digitale che permette ai residenti e ai visitatori di condividere informazioni, contenuti culturali e spunti turistici. Gli utenti possono anche evidenziare luoghi di interesse e pubblicare itinerari di visita personalizzati. La piattaforma è stata progettata per gestire contenuti relativi a una singola area comunale, semplificando l'interazione e la manutenzione dei contributi della comunità.

## Tecnologie e Strumenti
- **Linguaggio di Programmazione:** Java, per sviluppo robusto e orientato agli oggetti.
- **Framework:** Spring Boot per setup rapidi e Hibernate per ORM e gestione dati.
- **Database:** H2, leggero e in-memory, ideale per sviluppo e test rapidi.
- **Autenticazione:** Gestita da Spring Security con sessioni e form di login.
- **Controllo Accessi:** Implementato con Spring Boot Security per politiche basate sui ruoli.
- **Metodologia di Sviluppo:** Processo Unificato per un approccio iterativo e flessibile.
- **Strumenti di Progettazione:** Visual Paradigm per diagrammi UML e documentazione architetturale.

## Architettura del Sistema
- **Struttura del Back-end:** Utilizzo di API REST sviluppate con Spring Boot per la gestione dei dati e le funzionalità di sicurezza, supportando operazioni CRUD su entità come utenti e itinerari.
- **Database:** Impiego di H2 con Hibernate per il mapping ORM, memorizzando dati critici come utenti, contributi e itinerari.
- **Configurazione della Sicurezza:** Autenticazione e autorizzazione basate sui ruoli gestite tramite Spring Security per proteggere l'accesso alle funzionalità della piattaforma.

## Funzionalità
- **Ruoli Utente:** La piattaforma supporta vari livelli di accesso, come Turista Autenticato, Contributore e Gestore della Piattaforma, ognuno con permessi specifici per operazioni come la modifica dei ruoli e la gestione delle autorizzazioni.
- **Gestione dei Punti di Interesse (POI):** Utenti autorizzati possono gestire punti di interesse geografici, inserendo dettagli quali nome, descrizione, latitudine e longitudine.
- **Gestione Itinerari:** Gli utenti possono creare itinerari personalizzabili che incorporano vari punti di interesse, con funzionalità per aggiungere o rimuovere POI. Gli itinerari sono soggetti a un processo di approvazione.
- **Gestione dei Contest di Contribuzione:** La piattaforma facilita l'organizzazione di contest culturali e turistici, consentendo agli utenti di inviare contributi. Include sistemi per verificare l'autenticità delle partecipazioni e approvare i contributi che rispettano criteri prestabiliti, assicurando la selezione di contenuti di qualità.

## Organizzazione del Codice
- **Classi DTO:** Facilitano il trasferimento di dati tra client e server, ottimizzando le interazioni senza esporre le entità del database.
- **Classi Model:** Mappano le entità del database, definendo la struttura dati per l'ORM e la persistenza.
- **Classi Repository:** Forniscono metodi CRUD automatici e personalizzati per semplificare l'accesso ai dati.
- **Classi Service:** Implementano la logica di business, coordinando le operazioni tra i repository e servizi.
- **Classi Controller:** Gestiscono le richieste HTTP, mappandole ai servizi adeguati e formattando le risposte.

## File di Configurazione
- **Configurazioni di Sicurezza:** Specificano le politiche di autenticazione e autorizzazione, inclusi dettagli come i tipi di autenticazione supportati e la sicurezza degli endpoint.
- **Proprietà dell'Applicazione:** Contengono impostazioni essenziali come configurazioni del database, parametri per l'autenticazione JWT, e dettagli per l'invio di email.

## Connessione al Database H2
L'utilizzo del database H2, leggero e in-memory, è stato scelto per diverse ragioni legate allo sviluppo del progetto. La console H2 offre una piattaforma pratica per eseguire varie operazioni.

**Accesso alla Console H2:**
- **URL:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL:** `jdbc:h2:mem:DatabaseTest`
- **Username:** `admin`
- **Password:** `passw`

Con l'accesso alla console H2, è possibile eseguire query SQL, visualizzare la struttura del database, monitorare le prestazioni e fare debug e test delle query.


## Utilizzo di Postman per il Testing delle API
Per garantire il corretto funzionamento delle API del progetto "Digitalizzazione Territoriale", è stato impiegato Postman, uno strumento essenziale per simulare e testare le richieste HTTP. Per testare l'applicazione:
1. **Avvio del Backend:** Assicurati che il backend dell'applicazione sia in esecuzione. Questo di solito comporta l'avvio del server Spring Boot dal tuo ambiente di sviluppo o da un terminale.
2. **Importazione delle Richieste su Postman:** Importa le richieste preconfigurate nel Postman. Questo può essere fatto tramite il caricamento di un file di collezione Postman che contiene tutte le richieste API necessarie per interagire con l'applicazione.
3. **Esecuzione delle Richieste:** Una volta importate le richieste, è possibile eseguirle individualmente per testare diverse funzionalità dell'applicazione. Questo include operazioni CRUD su utenti, itinerari, punti di interesse e altri dati gestiti dall'applicazione.

## Considerazioni
- **Sicurezza:** Implementare un'autenticazione e un'autorizzazione robuste è stato fondamentale per garantire la sicurezza e l'integrità dei dati degli utenti.
- **Scalabilità:** Mentre attualmente la piattaforma supporta una singola area comunale, gli sviluppi futuri potrebbero espandersi a più regioni, richiedendo un'attenta considerazione della scalabilità del database e dell'applicazione.

## Conclusione
Il progetto sfrutta tecniche e tecnologie moderne di ingegneria del software per costruire una piattaforma sicura e funzionale per l'engagement della comunità e la promozione turistica.
