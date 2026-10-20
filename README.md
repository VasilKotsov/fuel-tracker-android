# Fuel Tracker Android

A minimal Android app for tracking fuel consumption, cost, and mileage calculations offline.

## First milestone

- Main form for liters, total price, distance, and distance unit selection
- Automatic calculation of fuel consumption and cost metrics
- Validation for invalid, zero, and negative inputs
- Unit tests for the calculator logic
- GitHub Actions workflow to build a debug APK

## Project structure

- `app/` — Android application source
- `app/src/main/java/com/example/fueltracker/` — app logic and Compose UI
- `app/src/test/java/com/example/fueltracker/` — JUnit tests
- `.github/workflows/` — CI workflow for Android builds

## Local development

```bash
chmod +x gradlew
./gradlew test
./gradlew assembleDebug
