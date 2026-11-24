# MedCalculator

[🇷🇺 На русском](README.ru.md)

MedCalculator is a Kotlin Multiplatform application for healthcare professionals that calculates pediatric medication dosages based on a patient's age and weight. It runs on Android, iOS, and in the browser as an installable PWA.

## Functionality
- **Medication Search**: Quick search and selection from a predefined list of medications.
- **Dosage Calculation**: Calculation in both milligrams (mg) and milliliters (ml) based on input parameters.
- **Safety Checks**: Validation against maximum single doses and specific age/weight limits for each medication.
- **Administration Routes**: Support for various routes (IV, IM, SC) with route-specific calculation rules.
- **Reference Info**: Access to detailed information about dosage regimens and administration methods.
- **Offline Web App**: the PWA is installable and keeps working without a network connection after the first load.
- **In-app Updates (Android)**: the app checks for a new version in RuStore on launch and offers a non-blocking update banner with download progress.

## Medication Data Sources
The dosage regimens and calculation algorithms are based on official data from the following registries:
- [State Register of Medicines (GRLS)](https://grls.minzdrav.gov.ru/).
- [GEOTAR Pharmaceuticals Reference](https://www.lsgeotar.ru/).
- [EAEU Register of SmPCs and Package Leaflets (OHLP)](https://lk.regmed.ru/Register/EAEU_SmPC).

## Supported platforms
- Android 9+ (RuStore).
- iOS 18.2+.
- Web: modern browsers with WebAssembly support. The PWA can be installed to the home screen and works offline.

## Tech Stack
- **Kotlin Multiplatform (KMP)**: Shared business logic across Android, iOS, and Web.
- **Compose Multiplatform**: Shared UI framework for building native-like interfaces on mobile and web.
- **Web target**: Kotlin/Wasm with Compose Multiplatform for Web.
- **PWA**: service worker with a generated precache manifest (`precache-manifest.json`) and offline cache for the app shell, Wasm modules, fonts, and medication data.
- **Analytics**: MyTracker on Android and Web.
- **App Updates**: RuStore In-app updates SDK with a Compose banner on Android.
- **Architecture**: Clean Architecture with MVVM and feature-first structure.
- **Dependency Injection**: Koin.
- **Persistence**: Room KMP on Android and iOS, DataStore with web local storage on the web.
- **Asynchronous Programming**: Kotlin Coroutines & Flow.
- **Serialization**: Kotlinx Serialization for JSON processing.
- **Preferences**: DataStore Preferences.
- **Deployment**: AWS CLI against Yandex Cloud Object Storage.

## Project Structure
- `:androidApp`: Android-specific application module, including the RuStore update controller and banner.
- `iosApp`: iOS-specific application module.
- `:shared`: The core module containing shared logic and UI.
    - `commonMain`:
        - `composeResources`:
            - `drawable`: Shared vector icons.
            - `files`: Contains `medications.json` — the medications data source.
            - `values`: Localization strings (`strings.xml`).
        - `kotlin/org/dishch/medcalculator`:
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
    - `androidMain`: Android-specific implementations (database driver, platform resources, update flow).
    - `iosMain`: iOS-specific implementations and native interop.
    - `wasmJsMain`: Web entry point (`Main.kt`), `index.html`, `manifest.json`, `sw.js`, PWA icons, and the webpack dev-server config.
    - `nonWebMain`: Data layer shared by Android and iOS (Room).
- `scripts/deploy_web.sh`: builds the web distribution and uploads it to Object Storage.
- `.env.template`: deployment configuration template. Real secrets live in the gitignored `.env`.

## Web Build and Deployment
Run the development server:
```sh
./gradlew :shared:wasmJsBrowserDevelopmentRun
```

Build and upload the production PWA:
```sh
cp .env.template .env   # once, then fill in the access keys
./scripts/deploy_web.sh
```

Upload an existing build without rebuilding:
```sh
./scripts/deploy_web.sh --skip-build
```

The script syncs `shared/build/dist/wasmJs/productionExecutable` to the bucket with `--delete`, sets a long-lived cache for hashed assets, `no-cache` for `index.html`, `sw.js`, `precache-manifest.json`, and `manifest.json`, and forces `Content-Type: application/wasm` for `.wasm` files.

Configuration (`.env`):
- `AWS_ACCESS_KEY_ID` — Object Storage static access key ID.
- `AWS_SECRET_ACCESS_KEY` — Object Storage static access key secret.
- `YC_S3_BUCKET` — bucket name, `medcalculatorapp.ru`.
- `AWS_DEFAULT_REGION` — `ru-central1`.
- `YC_S3_ENDPOINT` — `https://storage.yandexcloud.net`.
- `PWA_PUBLIC_URL` — public URL printed after a successful deploy.

The bucket must have public read access and static website hosting enabled with `index.html` as the index document. No CDN is used, so there is no cache invalidation step.

## Release Notes
### 1.0.1
- Fixed the bottom bar being clipped in the installed iOS web app (standalone PWA).

### 1.0.0
- Added the web version (Kotlin/Wasm + Compose Multiplatform) as an installable offline PWA.
- Added MyTracker analytics on Android and Web; it replaces Yandex Metrica on the web.
- Added RuStore In-app updates on Android with a non-blocking banner and download progress.
- Added the deployment script for Yandex Cloud Object Storage.

## Use Cases
1. **Emergency Dosage Calculation**: A paramedic needs to calculate the dose of Fentanyl for a 5-year-old child weighing 20kg. They select the medication, enter the parameters, and instantly get the exact volume in ml for IV administration.
2. **Medication Verification**: A nurse checks if a prescribed dose exceeds the maximum single dose defined in the official guidelines (e.g., for Sibazon).
