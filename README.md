# Music Library - Spring Boot Application

Harjoitustyö: Artistien ja albumien hallintajärjestelmä Spring Boot- ja Spring Security -frameworkilla toteutettuna.

## 📋 Ominaisuudet

### Arvosana 1 ✅
- **Spring Boot** - Sovelluksen perusta
- **JPA/Hibernate** - Tietokantakommunikointi
- **Thymeleaf** - HTML-näkymät tietokannasta haetulla datalla
- **MVC-arkkitehtuuri** - Selkeä rakenne

### Arvosana 2 ✅
- **CRUD-operaatiot** - Create, Read, Update, Delete kaikille entiteeteille
- **OneToMany-suhde** - Artist ↔ Albums

### Arvosana 3 ✅
- **Spring Security** - Kirjautuminen ja session-hallinta
- **Rooli-pohjainen pääsynvalvonta** - USER ja ADMIN roolit
- **Form Login** - Perinteinen kirjautumissivuilla

### Arvosana 4 ✅
- **Validointi** - @Valid ja @NotBlank, @Min, @NotNull
- **REST API** - GET, POST, PUT, DELETE -operaatiot
- **Pääsynvalvonta REST-APIissa** - Admin-only operaatiot

### Arvosana 5 ✅
- **JUnit-testit** - Security ja authorization testit
- **PostgreSQL-tietokanta** - Ulkoinen tietokanta tuotannossa
- **Deployment** - Render.com palvelimella
- **Monipuolinen testaus** - Controller- ja API-testit

## 🚀 Käynnistäminen

### Kehityksessa (H2-tietokanta)
```bash
mvn clean spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Tuotannossa (PostgreSQL)
```bash
mvn clean spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

## 🧪 Testit

```bash
# Kaikki testit
mvn test

# Vain Security testit
mvn test -Dtest=*SecurityTest

# Vain Repository testit
mvn test -Dtest=*RepositoryTest
```

## 📊 Testien kattavuus

- **ArtistRepositoryTest** - 2 testiä
- **AlbumRepositoryTest** - 2 testiä
- **ArtistControllerSecurityTest** - 8 testiä
- **ArtistRestControllerSecurityTest** - 7 testiä
- **Yhteensä: 19 testiä**

## 🔐 Kirjautuminen

### Demo-käyttäjät
```
USER:
  Käyttäjänimi: user
  Salasana: user123
  Roolit: USER
  Oikeudet: Voi vain katsella artisteja ja albumeita

ADMIN:
  Käyttäjänimi: admin
  Salasana: admin123
  Roolit: ADMIN, USER
  Oikeudet: Voi luoda, muokata ja poistaa artisteja ja albumeita
```

## 🌐 API-dokumentaatio

### Artists
- `GET /api/artists` - Hae kaikki artistit (USER+)
- `GET /api/artists/{id}` - Hae artisti ID:llä (USER+)
- `POST /api/artists` - Luo uusi artisti (ADMIN)
- `PUT /api/artists/{id}` - Päivitä artisti (ADMIN)
- `DELETE /api/artists/{id}` - Poista artisti (ADMIN)

### Albums
- `GET /api/albums` - Hae kaikki albumit (USER+)
- `GET /api/albums/{id}` - Hae albumi ID:llä (USER+)
- `POST /api/albums` - Luo uusi albumi (ADMIN)
- `PUT /api/albums/{id}` - Päivitä albumi (ADMIN)
- `DELETE /api/albums/{id}` - Poista albumi (ADMIN)

## 🗄️ Tietokantarakenne

```
ARTISTS (id, name)
  ↓ 1:N
ALBUMS (id, title, releaseYear, artist_id)
```

## 📦 Riippuvuudet

- Spring Boot 3.3.0
- Spring Data JPA
- Spring Security
- Thymeleaf
- PostgreSQL Driver
- H2 (Development)
- JUnit 5
- AssertJ

## 🔧 Konfiguraatio

### Profiilit
- `dev` - Kehitys (H2 in-memory database)
- `prod` - Tuotanto (PostgreSQL)

### Environment-muuttujat (Tuotanto)
```
DATABASE_URL=jdbc:postgresql://host:port/database
DATABASE_USER=username
DATABASE_PASSWORD=password
PORT=8080
```

## 📝 Tiedostot

```
src/
├── main/
│   ├── java/com/example/musiclibrary/
│   │   ├── controller/
│   │   │   ├── ArtistController.java
│   │   │   ├── AlbumController.java
│   │   │   ├── AuthController.java
│   │   │   └── api/
│   │   │       ├── ArtistRestController.java
│   │   │       └── AlbumRestController.java
│   │   ├── model/
│   │   │   ├── Artist.java
│   │   │   └── Album.java
│   │   ├── repository/
│   │   │   ├── ArtistRepository.java
│   │   │   └── AlbumRepository.java
│   │   ├── dto/
│   │   │   ├── ArtistRequest.java
│   │   │   ├── ArtistResponse.java
│   │   │   ├── AlbumRequest.java
│   │   │   └── AlbumResponse.java
│   │   ├── config/
│   │   │   └── SecurityConfig.java
│   │   └── MusicLibraryApplication.java
│   └── resources/
│       ├── templates/
│       │   ├── login.html
│       │   ├── artists.html
│       │   ├── artist-details.html
│       │   ├── artist-form.html
│       │   ├── albums.html
│       │   └── album-form.html
│       └── application*.properties
└── test/
    └── java/com/example/musiclibrary/
        ├── repository/
        │   ├── ArtistRepositoryTest.java
        │   └── AlbumRepositoryTest.java
        └── controller/
            ├── ArtistControllerSecurityTest.java
            └── api/
                └── ArtistRestControllerSecurityTest.java
```

## 🚀 Deployment (Render.com)

1. GitHub-repositorio linkitetty
2. Environment-muuttujat asetettu
3. PostgreSQL-instanssi luotu
4. Sovellus deployattu automaattisesti

## 👨‍💻 Tekijä
Music Library harjoitustyö - Spring Boot

## 📄 Lisenssi
Opetuskäyttö
