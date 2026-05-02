# NumberGame — Spring MVC Pur (sans Spring Boot)

## Architecture du projet

```
NumberGame_SpringMVC/
├── pom.xml                          ← packaging WAR (pas JAR)
└── src/main/
    ├── java/com/jeu/
    │   ├── config/
    │   │   ├── WebAppInitializer.java     ← Remplace web.xml
    │   │   ├── WebMvcConfig.java          ← Remplace dispatcher-servlet.xml
    │   │   ├── AppConfig.java             ← DataSource + JPA + Transactions
    │   │   ├── SecurityConfig.java        ← Spring Security
    │   │   └── SecurityWebAppInitializer.java
    │   ├── controller/
    │   │   ├── AuthController.java
    │   │   ├── AccueilController.java
    │   │   └── JeuController.java
    │   ├── service/
    │   │   ├── JoueurService.java
    │   │   └── PartieService.java
    │   ├── repository/
    │   │   ├── JoueurRepository.java
    │   │   └── PartieRepository.java
    │   ├── entity/
    │   │   ├── Joueur.java
    │   │   └── Partie.java
    │   └── model/
    │       └── JeuSession.java
    ├── resources/
    │   └── db.properties              ← Identifiants SQL Server
    └── webapp/
        ├── WEB-INF/templates/         ← Vues Thymeleaf
        │   ├── login.html
        │   ├── register.html
        │   ├── accueil.html
        │   ├── jeu.html
        │   └── historique.html
        └── static/
            ├── css/style.css
            └── images/
```

---

## Différence Spring Boot vs Spring MVC Pur

| | Spring Boot (interdit) | Spring MVC Pur ✅ |
|---|---|---|
| Packaging | JAR auto-exécutable | WAR → déployé sur Tomcat |
| Démarrage | `main()` avec `SpringApplication.run()` | Tomcat lit `WebAppInitializer` |
| Configuration | `application.properties` auto-magique | `AppConfig.java` + `WebMvcConfig.java` |
| Serveur | Tomcat embarqué | Tomcat externe |
| Annotation principale | `@SpringBootApplication` | `@Configuration` + `@EnableWebMvc` |

---

## Prérequis

- Java 17+
- Maven 3.8+
- **Apache Tomcat 10.x** (compatible Jakarta EE 9+)
- SQL Server (ou SQL Server Express)

---

## Étape 1 : Configurer la base de données

Créer la base dans SQL Server :
```sql
CREATE DATABASE numbergame_db;
```

Modifier `src/main/resources/db.properties` :
```properties
db.url=jdbc:sqlserver://localhost:1433;databaseName=numbergame_db;encrypt=false;trustServerCertificate=true
db.username=sa
db.password=VOTRE_VRAI_MOT_DE_PASSE
```

Les tables sont créées **automatiquement** par Hibernate (`ddl-auto=update`).

---

## Étape 2 : Compiler en WAR

```bash
mvn clean package -DskipTests
```

Le fichier `NumberGame.war` sera généré dans `target/`.

---

## Étape 3 : Déployer sur Tomcat

### Option A — Copier dans webapps/
1. Télécharger **Tomcat 10.x** sur https://tomcat.apache.org
2. Copier `target/NumberGame.war` dans le dossier `webapps/` de Tomcat
3. Démarrer Tomcat : `bin/startup.sh` (Linux/Mac) ou `bin/startup.bat` (Windows)
4. Accéder à : **http://localhost:8080/NumberGame**

### Option B — Tomcat Manager (interface web)
1. Aller sur http://localhost:8080/manager/html
2. Section "Déployer un fichier WAR" → choisir `NumberGame.war`
3. Cliquer "Déployer"

---

## Structure de la base de données

### Table `joueurs`
| Colonne | Type | Description |
|---|---|---|
| id | BIGINT PK | Identifiant |
| username | VARCHAR(255) UNIQUE | Nom d'utilisateur |
| password | VARCHAR(255) | Mot de passe hashé (BCrypt) |
| role | VARCHAR(50) | ROLE_USER |
| score_total | INT | Score cumulé |
| parties_gagnees | INT | Nombre de victoires |
| parties_jouees | INT | Total de parties |

### Table `parties`
| Colonne | Type | Description |
|---|---|---|
| id | BIGINT PK | Identifiant |
| joueur_id | BIGINT FK | Référence joueur |
| niveau | INT | 1=Facile, 2=Moyen, 3=Difficile |
| nombre_secret | INT | Le nombre à deviner |
| essais_utilises | INT | Essais effectués |
| essais_max | INT | Limite d'essais |
| range_min / range_max | INT | Plage de recherche |
| en_cours | BIT | true=sauvegardée, false=terminée |
| gagnee | BIT | Résultat |
| score | INT | Points obtenus |
| historique_essais | VARCHAR(1000) | Essais séparés par ";" |
| date_creation | DATETIME | Date création |
| date_sauvegarde | DATETIME | Dernière modification |

---

## Règles du jeu

- Un **nombre secret** est tiré aléatoirement
- Le joueur propose des nombres et reçoit un indice **"trop grand" ou "trop petit"**
- Stratégie optimale : la **dichotomie** (diviser la plage par 2 à chaque essai)

| Niveau | Plage | Essais max | Score base |
|---|---|---|---|
| Facile | 1 – 50 | 15 | 100 pts |
| Moyen | 1 – 100 | 10 | 250 pts |
| Difficile | 1 – 200 | 7 | 500 pts |

Score = Base + (essais restants × 10)
