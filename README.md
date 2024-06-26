# UniqueThreads webservice REST API Spring Boot project

Het platform waar je kleding kan kopen en een kledingwinkel kan beginnen in een webwinkelcentrum

## Inleiding

UniqueThreads is een platform ontworpen voor en door kledingontwerpers om hun unieke items te presenteren en te verkopen. Met een gebruiksvriendelijke interface stelt het ontwerpers in staat om hun eigen winkels te creëren, producten te beheren, voorraad bij te houden, transacties te verwerken en meer.

## Vereiste

Om de webservice te kunnen draaien, heb je de volgende benodigdheden nodig:

1.	Host en Poort: Deze applicatie draait op localhost:8080.
2.	IDE: De code is geschreven met behulp van IntelliJ IDEA.
3.	JDK 17: Zorg ervoor dat je Java Development Kit versie 17 hebt geïnstalleerd.
4.	Postman: Een tool voor het testen van de API endpoints. De bijbehorende Postman collectie is ook nodig.
5.	PostgreSQL: De database waar de backend services hun data opslaan.
6.	pgAdmin: Een management tool voor PostgreSQL, waarmee je de database kunt beheren.


## Installatie

Volg deze stappen om de webservice te installeren

1.	Installeer en open een IDE.
Voor het ontwikkelen van dit project is gebruikt gemaakt van IntelliJ IDEA.

2.	Zorg ervoor dat PostgreSQL en pgAdmin geïnstalleerd zijn.

3.	Maak een database met wachtwoord aan in pgAdmin.

4.	Navigeer naar de juiste locatie van je project en importeer het Project.

Open je IDE en kloon of importeer de codebase van de UniqueThreads Spring Boot REST API naar je lokale directory. De URL is te vinden in bijlage 1 “github link naar project UniqueThreads webservice”.

>>	git clone https://github.com/theGpoint7/Eindopdracht-Novi-Backend.git
> 
(Als je IDE vraagt welke build tool je wilt gebruiken kies dan voor maven.)

5.	Kopieer de code in het application.properties.example bestand (te vinden in: \src\main\resources) naar een nieuw application.properties bestand zodat je de database kunt instellen. 
6.	Gebruik de SecureStringGenerator te vinden in src\main\java\novi\backend\opdracht\backendservice\util\SecureStringGenerator.java om een singin key te genereren, kopieer deze naar je application.properties bestand:

jwt.secret="jouw-SecureStringGenerator-secret-key"

7.	Voer de gegevens van jouw database in: 

spring.datasource.url=jdbc:postgresql://"host-naam":"poort-nummer"/"database-naam" </br>
spring.datasource.username="gebruikersnaam-pgAdmin"</br>
spring.datasource.password="wachtwoord-database"</br>
	
Als je pgAdmin gebruikt, voer dan bij: 
-	Host-naam : localhost
-	Poort-nummer : 5432
-	Database-naam : de naam van je database
-	gebruikersnaam-pgAdmin : de gebruikersnaam van pgAdmin
-	wachtwoord-database : het wachtwoord van je database

8.	Bouw en Run de applicatie:
Gebruik IntelliJ IDEA om het project te bouwen. 

>>	mvn clean install


## Gebruikersrollen

•	CUSTOMER: Kan producten bekijken, bestellen en feedback geven.

•	DESIGNER: Kan eigen winkel beheren, producten toevoegen, promoties creëren en verkopen beheren.

•	ADMIN: Kan designer verzoeken beheren, rollen toewijzen en betaling statussen bijwerken.

## Testgebruikers
<table>
<tr>
<td>Gebruikersnaam</td>
<td>Wachtwoord</td>
<td>Rol</td>
</tr>
<tr>
<td>admin</td>
<td>Admin01!</td>
<td>CUSTOMER, ADMIN</td>
</tr><tr>
<td>klaartjee</td>
<td>Klaartjee!</td>
<td>CUSTOMER</td>
</tr><tr>
<td>petergriffin</td>
<td>Petergriffin01</td>
<td>CUSTOMER, DESIGNER</td>
</tr><tr>
<td>spiderman</td>
<td>Spiderman01</td>
<td>CUSTOMER, DESIGNER</td>
</tr>
</table>

## REST Endpoints

### Authentication Controller

GET /authenticated
Beschrijving: Controleert of de gebruiker geauthenticeerd is en geeft de gebruikersnaam en autoriteiten terug.
Rollen: GEAUTHENTICEERD

POST /authenticate
Beschrijving: Verifieert gebruikersreferenties en genereert een JWT-token.
Rollen: TOEGESTAAN VOOR IEDEREEN

### Cart Controller

POST /cart/add
Beschrijving: Voegt een product toe aan de winkelwagen van de gebruiker.
Rollen: DESIGNER, CUSTOMER

POST /cart/remove
Beschrijving: Verwijdert een product uit de winkelwagen van de gebruiker.
Rollen: DESIGNER, CUSTOMER

GET /cart
Beschrijving: Haalt de inhoud van de winkelwagen op.
Rollen: DESIGNER, CUSTOMER

PUT /cart/update
Beschrijving: Werkt de hoeveelheid van een product in de winkelwagen bij.
Rollen: DESIGNER, CUSTOMER

### Designer Controller

GET /designers
Beschrijving: Haalt een lijst van alle ontwerpers op.
Rollen: DESIGNER, CUSTOMER

GET /designers/{designerId}
Beschrijving: Haalt het profiel van een specifieke ontwerper op.
Rollen: DESIGNER, CUSTOMER

POST /designers/{designerId}/promotion
Beschrijving: Maakt een promotie aan voor een specifieke ontwerper.
Rollen: DESIGNER

GET /designers/{designerId}/promotions
Beschrijving: Haalt alle promoties van een specifieke ontwerper op.
Rollen: DESIGNER

PUT /designers/{designerId}/promotion/apply
Beschrijving: Past een promotie toe op een product.
Rollen: DESIGNER

GET /designers/{designerId}/sales
Beschrijving: Haalt de verkoopcijfers op van een specifieke ontwerper.
Rollen: DESIGNER

### Designer Request Controller

POST /designerrequest/request
Beschrijving: Indient een verzoek om ontwerper te worden.
Rollen: CUSTOMER

GET /designerrequest/request/{username}
Beschrijving: Haalt de ontwerpverzoeken van een specifieke gebruiker op.
Rollen: CUSTOMER

PUT /designerrequest/update
Beschrijving: Werkt de informatie van een ontwerper bij.
Rollen: DESIGNER

GET /designerrequest/requests/all
Beschrijving: Haalt alle ontwerpverzoeken op.
Rollen: ADMIN

POST /designerrequest/requests/{requestId}/approve
Beschrijving: Keurt een ontwerpverzoek goed of af.
Rollen: ADMIN

### Feedback Controller

POST /feedback
Beschrijving: Plaatst feedback over een product of designer.
Rollen: CUSTOMER

### Payment Controller

POST /payments/process/{orderId}
Beschrijving: Verwerkt de betaling voor een specifieke bestelling.
Rollen: DESIGNER, CUSTOMER

POST /payments/confirm/{orderId}
Beschrijving: Bevestigt de betaling voor een specifieke bestelling.
Rollen: ADMIN

### Order Controller

POST /orders
Beschrijving: Plaatst een nieuwe bestelling.
Rollen: DESIGNER, CUSTOMER

GET /orders/{orderId}
Beschrijving: Haalt de details van een specifieke bestelling op.
Rollen: DESIGNER, CUSTOMER

GET /orders/user
Beschrijving: Haalt de bestellingen van de huidige gebruiker op.
Rollen: DESIGNER, CUSTOMER

PUT /orders/{orderId}/cancel
Beschrijving: Annuleert een specifieke bestelling.
Rollen: DESIGNER, CUSTOMER

PUT /orders/{orderId}/confirm-shipment
Beschrijving: Bevestigt de verzending van een specifieke bestelling.
Rollen: DESIGNER

GET /orders/{orderId}/receipt
Beschrijving: Haalt de bon van een specifieke bestelling op.
Rollen: DESIGNER, CUSTOMER

### Product Controller

GET /products
Beschrijving: Haalt een lijst van alle producten op, eventueel gefilterd op maat, kleur of winkelnaam.
Rollen: DESIGNER, CUSTOMER

GET /products/{productId}
Beschrijving: Haalt de details van een specifiek product op.
Rollen: DESIGNER, CUSTOMER

POST /products
Beschrijving: Maakt een nieuw product aan.
Rollen: DESIGNER

PUT /products/{productId}
Beschrijving: Werkt een specifiek product bij.
Rollen: DESIGNER

### User Controller

POST /users
Beschrijving: Maakt een nieuwe gebruiker aan.
Rollen: TOEGESTAAN VOOR IEDEREEN

GET /users/all
Beschrijving: Haalt een lijst van alle gebruikers op.
Rollen: ADMIN, CUSTOMER

GET /users/{username}
Beschrijving: Haalt de gegevens van een specifieke gebruiker op.
Rollen: ADMIN, CUSTOMER

PUT /users/{username}/update-information
Beschrijving: Werkt de informatie van een specifieke gebruiker bij.
Rollen: ADMIN, CUSTOMER

PUT /users/{username}/update-password
Beschrijving: Werkt het wachtwoord van een specifieke gebruiker bij.
Rollen: ADMIN, CUSTOMER

GET /users/{username}/authorities
Beschrijving: Haalt de autoriteiten van een specifieke gebruiker op.
Rollen: ADMIN

POST /users/{username}/authorities
Beschrijving: Voegt een autoriteit toe aan een specifieke gebruiker.
Rollen: ADMIN

DELETE /users/{username}/authorities/ROLE_*
Beschrijving: verwijdert een autoriteit van een specifieke gebruiker.
Rollen: ADMIN

PUT /users/{username}/disable-account
Beschrijving: Deactiveert het account van een specifieke gebruiker.
Rollen: ADMIN

## Testen

De functionaliteit van de order en abstractproduct entiteiten zijn uitvoerig getest mbv integration tests en unit tests met een 100% line coverage.