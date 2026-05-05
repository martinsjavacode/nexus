# Project Structure

```
src/main/java/io/github/martinsjavacode/nexus/
├── NexusApplication.java          # Spring Boot entry point
├── api/                           # REST controllers
│   └── NexusController.java
├── domain/                        # Request/response DTOs and domain models
│   ├── NexusRequest.java          # Record: message, tone
│   ├── NexusResponse.java         # Record: content, metadata map
│   └── model/                     # (empty) Reserved for domain entities
└── service/                       # Business logic interfaces
    ├── AiService.java             # AI service contract
    └── impl/                      # Concrete implementations
        └── GeminiAiService.java   # Gemini-backed implementation

src/main/resources/
└── application.yaml               # Spring Boot + Spring AI config

src/test/java/io/github/martinsjavacode/nexus/
└── NexusApplicationTests.java     # Spring context smoke test
```

## Package Conventions

- **Base package**: `io.github.martinsjavacode.nexus`
- **`api`** — REST controllers. Annotated with `@RestController`. Handles HTTP concerns only; delegates to services.
- **`domain`** — DTOs and domain models. Uses Java records for immutable data carriers.
- **`domain.model`** — Reserved for richer domain entities as the project evolves.
- **`service`** — Interfaces defining business operations.
- **`service.impl`** — Concrete service implementations. Annotated with `@Service`. New AI providers go here as additional implementations of `AiService`.

## Architecture Patterns

- **Interface-based services**: All services are defined as interfaces with separate `impl` classes. Follow this pattern when adding new services.
- **Constructor injection**: Dependencies are injected via constructors (no `@Autowired` on fields).
- **Records for DTOs**: Use Java records for request/response objects.
- **Layered architecture**: Controller → Service → AI Model. Controllers should not contain business logic.
