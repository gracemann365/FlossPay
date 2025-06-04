# Contributing to FlossPay

Thank you for your interest in contributing to FlossPay!

## How to Contribute

1. **Fork the repo** and create a new branch:  
   `feature/<topic>` or `fix/<ticket#>` (strict branch convention).

2. **Run pre-commit checks:**
   ```bash
   ./scripts/pre-commit.sh
   ```

This runs lint, static analysis, unit/integration tests, and security scan.

3. **Open a Pull Request:**
   Target the `main` branch. Your PR must:

   - Pass all CI checks (lint, test, coverage, security)
   - Receive review from a core maintainer

4. **Staging validation:**
   All merges are auto-deployed to staging for peer validation before production.

## Governance

- All issues, PRs, and merges must use the provided templates.
- Review and merges are peer-gated—no silent merges.
- For security concerns, see [SECURITY.md](SECURITY.md).

## Code Style & Standards

- Use Java 21 and Spring Boot 3.x idioms.
- Write clear, inline JavaDoc for all public classes and methods.
- Tests are required for all new logic—unit, integration, or E2E.
- Follow the [Code of Conduct](CODE_OF_CONDUCT.md).

## Community

Be excellent to each other!
Questions or ideas? Use GitHub Discussions or open an RFC.

---
