# Fuel Tracker Android

A minimal Android app for tracking fuel consumption and costs offline.

## First milestone

- Main input form for liters, total price, distance, and unit selection
- Automatic calculations for fuel consumption, cost per kilometer, and price per liter
- Validation for invalid and unsafe values
- JUnit tests for the calculator
- GitHub Actions workflow for a debug APK build

## Local build

```bash
./gradlew test
./gradlew assembleDebug
```
