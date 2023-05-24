# iskam

Toto je README soubor pro Vaši Spring Boot aplikaci, která využívá Docker a PostgreSQL databázi.

## Popis

Tato aplikace poskytuje API rozhraní pro přehled jídel a jejich alergenů. Používá Spring Boot framework a je integrovaná s Dockerem a PostgreSQL databází.

## Spuštění aplikace

1. Nejprve si ujistěte, že máte nainstalovaný Docker a PostgreSQL na svém systému.
2. Naklonujte si tento repozitář na svůj lokální systém.
3. Otevřete terminál a přejděte do složky s projektem.
4. V kořenové složce projektu spusťte příkaz `docker-compose up -d`, který spustí kontejnery s aplikací a PostgreSQL databází.
5. Po úspěšném spuštění můžete přistupovat k API na adrese [http://localhost:9000/api/v1](http://localhost:9000/api/v1).

## Koncové body (Endpoints)

### Food Controller

#### Získání seznamu jídel

**URL:** `/api/v1/appFood`

**Metoda:** `GET`

**Popis:** Tato metoda vrátí seznam všech jídel.

#### Získání konkrétního jídla

**URL:** `/api/v1/appFood/{id}`

**Metoda:** `GET`

**Popis:** Tato metoda vrátí konkrétní jídlo podle zadaného ID.

#### Vytvoření nového jídla

**URL:** `/api/v1/appFood`

**Metoda:** `POST`

**Popis:** Tato metoda vytvoří nové jídlo na základě zadaných informací.

#### Aktualizace existujícího jídla

**URL:** `/api/v1/appFood/{id}`

**Metoda:** `PUT`

**Popis:** Tato metoda aktualizuje existující jídlo podle zadaného ID.

#### Smazání jídla

**URL:** `/api/v1/appFood/{id}`

**Metoda:** `DELETE`

**Popis:** Tato metoda smaže existující jídlo podle zadaného ID.

### Category Controller

#### Získání seznamu kategorií

**URL:** `/api/v1/appCategory`

**Metoda:** `GET`

**Popis:** Tato metoda vrátí seznam všech kategorií.

#### Získání konkrétní kategorie

**URL:** `/api/v1/appCategory/{id}`

**Metoda:** `GET`

**Popis:** Tato metoda vrátí konkrétní kategorii podle zadaného ID.

#### Vytvoření nové kategorie

**URL:** `/api/v1/appCategory`

**Metoda:** `POST`

**Popis:** Tato metoda vytvoří novou kategor.
