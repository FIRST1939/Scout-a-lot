# Architecture

Android and iOS applications with one shared KMP module. All shareable code lives in `shared`, organized by feature packages rather than extra modules.

## Structure

```text
2026Scouting/
├── androidApp/                # Android application and platform integration
├── iosApp/                    # iOS application
├── shared/                    # KMP module: shared data, domain, and viewmodels
│   ├── commonMain/            # laid out by feature; see below
│   ├── androidMain/           # Android platform implementations
│   ├── iosMain/               # iOS platform implementations
│   └── schemas/               # exported Room schemas; update with every database change
├── docs/
└── gradle/libs.versions.toml
```

## commonMain layout

Feature-first: one `core/` package for feature-agnostic infrastructure, everything else organized by feature. Base package is `com.frcteam1939.scouting2026`.

```text
commonMain/kotlin/com/frcteam1939/scouting2026/
├── core/                          # infrastructure that serves no single feature
│   ├── database/                  # persistence and database infrastructure
│   ├── network/                   # network clients and data exchange
│   └── storage/                   # platform-independent storage contracts
├── feature/
│   ├── settings/                  # application settings
│   ├── events/                    # event management
│   ├── scouting/                  # scouting workflows and data collection
│   ├── forms/                     # form configuration
│   ├── reports/                   # reports and analysis
│   └── sync/                      # data synchronization
└── AppContainer.kt                # application-level dependency composition
```

- Dependencies flow one way: `feature → core`. Features never import each other; shared models either move to `core` or live in the consumed feature's `model/` package.
- Inside a feature, keep it flat: viewmodels, repositories, and models. Add `ui/` per feature once Compose Multiplatform lands.
- Feature-specific code belongs in `feature/`; reusable infrastructure belongs in `core/`.
- expect/actual files keep the same package path across source sets.

## Rules

- Layers: Screen → ViewModel → Repository → DAO / API client. ViewModels never touch DAOs or network clients directly.
- Repositories expose reads as `Flow` and writes as `suspend`.
- Room entities and network DTOs stay inside their layer; convert to models before exposing them to UI.
- Platform-specific code goes in `androidMain` / `iosMain` via expect/actual. Android-only features with no shared future (Bluetooth) stay in `androidApp`.
- Database changes ship with an updated exported schema in `shared/schemas/`.
