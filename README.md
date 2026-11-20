# Archna Kumari — Full-Stack Portfolio

A responsive personal portfolio served by Spring Boot. The contact form sends data to a Java REST API. Messages are stored in a local SQLite database and exported to an Excel workbook.

## Technology stack

- Java 17
- Spring Boot 3.5.7
- Spring Web and Bean Validation
- Spring Data JPA / Hibernate Community Dialects
- SQLite (Xerial JDBC)
- Apache POI for `.xlsx` export
- HTML, CSS and vanilla JavaScript

## Project structure

```text
src/main/java/com/archna/portfolio/
├── PortfolioApplication.java
├── contact/
│   ├── ContactController.java
│   ├── ContactMessage.java
│   ├── ContactRepository.java
│   ├── ContactRequest.java
│   ├── ContactResponse.java
│   ├── ContactService.java
│   └── ExcelContactWriter.java
└── exception/
    └── GlobalExceptionHandler.java

src/main/resources/
├── application.properties
└── static/
    ├── index.html
    ├── css/style.css
    ├── js/main.js
    └── images/
```

## Requirements

1. JDK 17 or later
2. Maven 3.6.3 or later (or use the Maven wrapper if you add one)
3. STS / Spring Tool Suite or another Java IDE

No separate SQLite server or MySQL installation is required. SQLite is an embedded database; the application creates the database file when it starts and connects successfully.

## Run in STS

1. Extract the ZIP file.
2. Open STS.
3. Select **File → Import → Maven → Existing Maven Projects**.
4. Browse to the extracted `archna-portfolio` folder and select `pom.xml`.
5. Click **Finish** and wait for Maven dependencies to download.
6. Open `src/main/resources/application.properties` and review the database path.
7. Right-click `PortfolioApplication.java` → **Run As → Spring Boot App**.
8. Open <http://localhost:8080>.

## Run from terminal

From the project root:

```bash
mvn clean spring-boot:run
```

Build a JAR:

```bash
mvn clean package
java -jar target/archna-portfolio-1.0.0.jar
```

## Database and Excel files

The default SQLite database file is:

```text
./data/archna_portfolio.db
```

The default Excel workbook is:

```text
./data/contacts.xlsx
```

Both paths are configurable through environment variables:

- `APP_DB_PATH` — SQLite database file path
- `CONTACTS_EXCEL_FILE` — Excel workbook path
- `PORT` — server port (defaults to `8080`)

The SQLite JDBC URL and Hibernate dialect are configured together in `application.properties`:

```properties
spring.datasource.url=jdbc:sqlite:${APP_DB_PATH:./data/archna_portfolio.db}
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
```

The Hibernate Community Dialects and SQLite JDBC dependencies are both declared in `pom.xml`. Keep their versions managed by the Spring Boot parent to avoid incompatible Hibernate versions.

## Contact API

`POST /api/contact`

Example JSON body:

```json
{
  "name": "A visitor",
  "email": "visitor@example.com",
  "message": "I would like to discuss an opportunity."
}
```

The API validates the request, stores it in the `contact_messages` table, and appends it to the Excel workbook. Validation errors return HTTP 400. Successful submissions return HTTP 201.

## Important production notes

- Do not commit database files, Excel exports, credentials, or `.env` files to a public GitHub repository.
- SQLite is suitable for a small portfolio contact form. For a multi-instance or higher-traffic deployment, use a managed server database such as PostgreSQL.
- Use persistent storage for both the SQLite database and Excel workbook. Many hosting providers use ephemeral filesystems that can erase local files after redeployments.
- Before public launch, add spam protection/rate limiting, backups, and an appropriate privacy notice. Do not expose the database or Excel file as a public static resource.

## GitHub

Create an empty GitHub repository, then run these commands from the project root (replace the URL with your own repository URL):

```bash
git init
git add .
git commit -m "Initial portfolio website"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
git push -u origin main
```
