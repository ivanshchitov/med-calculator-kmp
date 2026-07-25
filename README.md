# MedCalculator

[🇷🇺 На русском](README.ru.md)

MedCalculator is a Kotlin Multiplatform mobile application designed for healthcare professionals to calculate pediatric medication dosages based on a patient's age and weight.

## Functionality
- **Medication Search**: Quick search and selection from a predefined list of medications.
- **Dosage Calculation**: Calculation in both milligrams (mg) and milliliters (ml) based on input parameters.
- **Safety Checks**: Validation against maximum single doses and specific age/weight limits for each medication.
- **Administration Routes**: Support for various routes (IV, IM, SC) with route-specific calculation rules.
- **Reference Info**: Access to detailed information about dosage regimens and administration methods.

## Medication Data Sources
The dosage regimens and calculation algorithms are based on official data from the following registries:
- [State Register of Medicines (GRLS)](https://grls.minzdrav.gov.ru/).
- [GEOTAR Pharmaceuticals Reference](https://www.lsgeotar.ru/).
- [EAEU Register of SmPCs and Package Leaflets (OHLP)](https://lk.regmed.ru/Register/EAEU_SmPC).

## Supported platforms
- Android 9+,
- iOS 18.2+.

## Tech Stack
- **Kotlin Multiplatform (KMP)**: Shared business logic across Android and iOS.
- **Compose Multiplatform**: Shared UI framework for building native-like interfaces on both platforms.
- **Architecture**: Clean Architecture with MVVM and feature-first structure.
- **Dependency Injection**: Koin.
- **Persistence**: Room KMP for local database storage.
- **Asynchronous Programming**: Kotlin Coroutines & Flow.
- **Serialization**: Kotlinx Serialization for JSON processing.
- **Preferences**: DataStore Preferences.

## Project Structure
- `:androidApp`: Android-specific application module.
- `iosApp`: iOS-specific application module.
- `:shared`: The core module containing shared logic and UI.
    - `commonMain`:
        - `composeResources`:
            - `drawable`: Shared vector icons.
            - `files`: Contains `medications.json` — the medications data source.
            - `values`: Localization strings (`strings.xml`).
        - `kotlin/org.dishch.medcalculator`:
            - `data`:
                - `local`: Room KMP database, DAOs, and Entity definitions.
                - `repository`: Implementations of domain repositories.
                - `PreferenceManager.kt`: DataStore-based preference management.
            - `di`: Koin modules for dependency injection.
            - `domain`:
                - `model`: Pure Kotlin data classes (Medication, CalculationResult, DosageRule).
                - `repository`: Abstract interfaces defining data operations.
                - `usecase`: Core business logic (e.g., calculations, filtering medications).
            - `ui`:
                - `components`: Reusable UI elements (custom cards like `WeightCard`, input fields, list items).
                - `helpers`: UI formatters and presentation logic.
                - `screens`: Feature-based UI modules (Main, Choose Medication, Results, About).
                - `theme`: Material 3 design system implementation (Color, Type, Shape).
            - `AppNavigation.kt`: Navigation graph and route definitions using Compose Navigation.
            - `App.kt`: Root Composable for the application.
    - `androidMain`: Android-specific implementations (e.g., database driver, platform-specific resources).
    - `iosMain`: iOS-specific implementations and native interop.

## Use Cases
1. **Emergency Dosage Calculation**: A paramedic needs to calculate the dose of Fentanyl for a 5-year-old child weighing 20kg. They select the medication, enter the parameters, and instantly get the exact volume in ml for IV administration.
2. **Medication Verification**: A nurse checks if a prescribed dose exceeds the maximum single dose defined in the official guidelines (e.g., for Sibazon).
