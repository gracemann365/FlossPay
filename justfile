# ──────────────────────────────────────────────────────────────────────────────
# FlossPay / OpenPay UPI Gateway rail — Developer Justfile
#
#   Shortcuts for everyday local development.
#   Run 'just --list' to see all commands.
#   See comments for extra tips and best practices.
# ──────────────────────────────────────────────────────────────────────────────

# ── Project Build Commands ──────────────────────────────────────────────

# Build the entire project (clean first, skip tests for speed)
build:
  mvn clean install -DskipTests

# Build only api-service (skip tests)
builda:
  mvn clean install -pl api-service -DskipTests

# Build only worker-service (skip tests)
buildw:
  mvn clean install -pl worker-service -DskipTests

# ── Running Services Locally ───────────────────────────────────────────

# Run the api-service locally using Spring Boot
runa:
  mvn spring-boot:run -pl api-service

# Run the worker-service locally using Spring Boot
runw:
  mvn spring-boot:run -pl worker-service

# ── Testing Commands ───────────────────────────────────────────────────

# Run all tests in the project
test:
  mvn test

# Run only api-service tests
testa:
  mvn test -pl api-service

# Run only worker-service tests
testw:
  mvn test -pl worker-service

# ── Clean Project ──────────────────────────────────────────────────────

# Remove target/ build artifacts from all modules
clean:
  mvn clean

# ── Database/Migration Shortcuts (expand as needed) ────────────────────

# Run Flyway migrations for api-service (if configured)
migrate:
  mvn -pl api-service flyway:migrate

# ───────────────────────────────────────────────────────────────────────
# Add your own shortcuts below!
# For advanced usage, see: https://github.com/casey/just
# ───────────────────────────────────────────────────────────────────────

# Developer Note:
# For a dedicated testing approach, see test/ or scripts/ in a future update.
