# GEMINI.md

# Kotlin Multiplatform Project Rules

## Project Overview

Target platforms:

- Android
- iOS

Architecture:

- Clean Architecture
- MVVM
- Feature-first structure

## Required Technologies

### Core

- Kotlin Multiplatform (KMP)
- Kotlin 2.x
- Compose Multiplatform
- Kotlin Coroutines
- Kotlinx Serialization
- Koin
- Room KMP
- DataStore Preferences

### Android

- Compose Multiplatform for Android
- AndroidX Lifecycle
- AndroidX Navigation

### iOS

- Compose Multiplatform for iOS
- Kotlin/Native

## Architecture Rules

Layers:

Presentation -> Domain -> Data

Dependencies may only point downward.

### Presentation Layer

Contains:

- Screens
- Composables
- ViewModels
- UI State

Rules:

- UI never accesses Room.
- UI never accesses DAOs.
- UI communicates through ViewModels.
- Expose StateFlow only.
- UI state must be immutable.

### Domain Layer

Contains:

- Use Cases
- Repository Interfaces
- Domain Models

Rules:

- No Android APIs.
- No iOS APIs.
- No Room dependencies.
- No Compose dependencies.

### Data Layer

Contains:

- Repository Implementations
- Room Database
- DAOs
- Mappers

Rules:

- Room entities stay in Data layer.
- Domain models are exposed outside Data layer.
- Use mapping between Entity and Domain models.

## Room Database Rules

Use Room KMP as the only database solution.

Required libraries:

- androidx.room:room-runtime
- androidx.room:room-ktx
- androidx.room:room-compiler
- androidx.sqlite

Rules:

- Every table must have a DAO.
- Every schema change requires a migration.
- Use Flow for observable queries.
- Use suspend functions for writes.
- Use transactions for multi-step operations.
- Avoid raw SQL unless necessary.

Entity -> Mapper -> Domain Model -> UI State

Never expose:

- Entity
- DAO
- RoomDatabase

outside Data layer.

## Dependency Injection

Use Koin.

Rules:

- Database is singleton.
- DAOs are singleton.
- Repositories are singleton.
- Use Cases are factories.
- Depend on interfaces.

## State Management

Use:

- StateFlow
- MutableStateFlow (private)

Rules:

- Never expose MutableStateFlow.
- Keep state immutable.
- Use data classes.

## Coroutines

Rules:

- Use structured concurrency.
- Never use GlobalScope.
- Use suspend functions.
- Use Flow for streams.
- Use Dispatchers.IO for database operations.
- Respect cancellation.

## Compose Multiplatform

Rules:

- Prefer stateless composables.
- Hoist state.
- Create reusable components.
- Keep composables small.
- Avoid business logic in UI.

## Navigation

Rules:

- Navigation belongs to Presentation.
- Repositories never trigger navigation.
- Centralize route definitions.

## Testing

Libraries:

- kotlin.test
- kotlinx-coroutines-test
- app.cash.turbine

Rules:

- Test all Use Cases.
- Test ViewModels.
- Test repository implementations.
- Prefer fake implementations over mocks.

## Platform Separation

Rules:

- Shared business logic belongs in commonMain.
- Android-specific code belongs in androidMain.
- iOS-specific code belongs in iosMain.
- Use expect/actual when platform APIs are required and when Compose Multiplatform is not suitable.

## Code Generation Rules

When generating code:

1. Follow Clean Architecture.
2. Use Kotlin Multiplatform APIs.
3. Use Room KMP for persistence.
4. Use Koin for dependency injection.
5. Use Compose Multiplatform UI.
6. Use Coroutines and Flow.
7. Use immutable models.
8. Create repository interfaces in Domain.
9. Create repository implementations in Data.
10. Generate tests.
11. Avoid platform-specific code in commonMain.
12. Produce production-ready code.
