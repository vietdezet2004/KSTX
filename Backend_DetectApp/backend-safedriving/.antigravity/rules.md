# Antigravity Master Rules: Java Spring Boot & Context Control

## 1. Mandatory Plan-First & Approval Protocol
- **Propose Solution First:** For any request involving code creation, modification, or deletion, present a concise step-by-step plan or solution outline BEFORE making any edits.

## 2. Context & Memory Retention (Chống quên & chống lạc context)
- **Scope Focus:** Focus strictly on the current active task. Do not touch or refactor files unrelated to the prompt.
- **Check Project Root:** Check structural configs (`pom.xml`, `build.gradle`, `README.md`) before making architectural decisions.
- **Task Tracking:** For complex tasks, display a simple markdown checklist and update task statuses as you progress.
- **Loop Prevention:** If an edit causes a compile or runtime error, revert the change immediately and propose an alternative instead of repeatedly trying the same broken pattern.

## 3. Code Modification Boundaries (Chống sửa lung tung)
- **Minimal Diff Rule:** Modify ONLY lines of code that are strictly necessary.
- **Preserve Existing Code:** Do NOT delete working code, existing utility methods, or imports unless replacing them with upgraded logic.
- **Dependency Guard:** Always ask for permission before adding new libraries or dependencies to `pom.xml` / `build.gradle`.
- **No Assumptions:** If business requirements are ambiguous, ask 1 clear clarifying question instead of making wild assumptions.

## 4. Java Spring Boot Architecture & Code Style
- **Strict Layering:**
  - `Controller`: Handle HTTP endpoints, DTO validation (`@Valid`), and route mappings ONLY. No business logic.
  - `Service`: Core business logic, transactional boundaries (`@Transactional`), and entity mapping.
  - `Repository`: Spring Data JPA interfaces. Use JPQL/Native queries when method names become unwieldy.
- **Coding Conventions:** Standard Java naming (CamelCase for variables/methods, PascalCase for classes, UPPER_SNAKE for constants).
- **Lombok Usage:** Use `@Getter`, `@Setter`, `@RequiredArgsConstructor`, `@Builder`. AVOID `@Data` on JPA Entities to prevent infinite recursion in `hashCode`/`equals`/`toString`.
- **Dependency Injection:** Use Constructor Injection (`@RequiredArgsConstructor` with `private final` fields). Avoid `@Autowired` on fields.
- **Data Transfer:** Use DTOs or Java `record` for request/response payloads. Never expose JPA Entities directly via API endpoints.
- **Error Handling:** Centralize exception handling via `@RestControllerAdvice` and `@ExceptionHandler`. Wrap output in a unified API response envelope (e.g., `ApiResponse<T>`).
- **No Placeholders:** Write 100% complete, compilable implementations. Never leave `// TODO` or placeholder code.
- **Unit Testing:** Write corresponding JUnit 5 and Mockito tests when implementing new features.

## 5. Output Style & Token Efficiency
- Be concise and direct. Prioritize direct code diffs over long preambles.
- Auto-format generated code according to standard Java formatting after user approves execution.