# Tech Stack & Build

## Core

- **Language**: Java 25
- **Framework**: Spring Boot 3.5.10
- **AI Integration**: Spring AI 1.1.2 (Google GenAI / Gemini starter)
- **Build Tool**: Maven (via Maven Wrapper — `./mvnw`)

## Dependencies

| Dependency | Purpose |
|---|---|
| `spring-boot-starter-web` | REST API (embedded Tomcat) |
| `spring-boot-devtools` | Hot reload during development |
| `spring-ai-starter-model-google-genai` | Gemini LLM integration via Spring AI |
| `spring-boot-starter-test` | JUnit 5, Mockito, Spring test support |

## Environment Variables

- `GEMINI_API_KEY` — Required. Google Gemini API key, referenced in `application.yaml`.

## Common Commands

```bash
# Compile
./mvnw compile

# Run tests
./mvnw test

# Package (build JAR)
./mvnw package

# Run the application
./mvnw spring-boot:run

# Clean build artifacts
./mvnw clean
```

## Code Style

- Java files use **tab** indentation (see `.editorconfig`).
- YAML files use **2-space** indentation.
- Max line length: **120** characters.
- Charset: **UTF-8**, line endings: **LF**.
- No final newline inserted at end of files.
