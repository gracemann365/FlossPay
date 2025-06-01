# Shared Libs Module

This `shared-libs` module contains all code and definitions that must be **shared across multiple OpenPay services**.  
It provides a **single source of truth** for data contracts, custom exceptions, validation logic, and other reusable components.

---

## What Belongs Here

- **DTOs:**  
  Data Transfer Objects used by multiple modules for consistent serialization, deserialization, and validation (e.g., `PaymentRequest`, `StatusResponse`).
- **Custom Exceptions:**  
  Application-specific exceptions (e.g., `OpenPayException`, `InvalidUpiException`) for unified error handling.
- **Validation Logic:**  
  Custom annotations and validators (e.g., `@ValidUpi`, `UpiValidator`) for shared data validation rules.
- **(Optionally) Utilities:**  
  Reusable utility classes and functions that are generic and needed in multiple modules.

---

## What Should NOT Be Here

- Service-specific business logic or classes only used in one module.
- Persistence layer entities or DAOs.
- Configurations or resources tied to a single service’s lifecycle.

---

## Why Use a Shared Libs Module?

- **Consistency:**  
  Keeps data contracts and error definitions in sync between modules (API, Worker, etc).
- **Maintainability:**  
  Update DTOs, exceptions, or validators in one place—no duplication, no drift.
- **Scalability:**  
  Easily add new services or features that need to use the same types or logic.

---

## How to Use

1. Add `shared-libs` as a dependency in any service module that requires shared DTOs or validation.
2. Import DTOs, exceptions, and validators directly from the shared package.
3. Add new shared logic here as your system evolves.

---

## TL;DR

- Put **shared DTOs, exceptions, and validators** here.
- **Don’t** add module-specific or business-only code.
- This is the “glue” for consistent inter-module communication and validation.

---

_— David Grace_
