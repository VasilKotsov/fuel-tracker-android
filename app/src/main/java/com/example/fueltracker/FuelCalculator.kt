package com.example.fueltracker

import java.math.BigDecimal
import java.math.RoundingMode

enum class DistanceUnit {
    KILOMETERS,
    MILES
}

data class FuelCalculationResult(
    val distanceKm: BigDecimal? = null,
    val totalCost: BigDecimal? = null,
    val fuelConsumption: BigDecimal? = null,
    val fuelEfficiency: BigDecimal? = null,
    val pricePerLiter: BigDecimal? = null,
    val costPerKilometer: BigDecimal? = null,
    val costPer100Km: BigDecimal? = null,
    val errorMessage: String? = null,
)

object FuelCalculator {
    private val ZERO = BigDecimal.ZERO
    private val HUNDRED = BigDecimal("100")
    private val MILES_TO_KM = BigDecimal("1.609344")

    fun convertDistanceToKilometers(value: BigDecimal, unit: DistanceUnit): BigDecimal {
        return if (unit == DistanceUnit.MILES) value.multiply(MILES_TO_KM) else value
    }

    fun calculate(
        litersText: String,
        pricePerLiterText: String,
        distanceText: String,
        distanceUnit: DistanceUnit,
    ): FuelCalculationResult {
        val liters = litersText.toBigDecimalOrNull()
        val pricePerLiter = pricePerLiterText.toBigDecimalOrNull()
        val distance = distanceText.toBigDecimalOrNull()

        if (liters == null || pricePerLiter == null || distance == null) {
            return FuelCalculationResult(errorMessage = "Please enter valid numeric values.")
        }

        if (distance <= ZERO) {
            return FuelCalculationResult(errorMessage = "Distance must be greater than zero.")
        }

        if (liters <= ZERO) {
            return FuelCalculationResult(errorMessage = "Fuel volume must be greater than zero.")
        }

        if (pricePerLiter < ZERO) {
            return FuelCalculationResult(errorMessage = "Price per liter cannot be negative.")
        }

        val distanceKm = convertDistanceToKilometers(distance, distanceUnit)
        val totalCost = liters.multiply(pricePerLiter)
        val fuelConsumption = liters.divide(distanceKm, 6, RoundingMode.HALF_UP)
            .multiply(HUNDRED)
        val fuelEfficiency = distanceKm.divide(liters, 6, RoundingMode.HALF_UP)
        val costPerKilometer = totalCost.divide(distanceKm, 6, RoundingMode.HALF_UP)
        val costPer100Km = costPerKilometer.multiply(HUNDRED)

        return FuelCalculationResult(
            distanceKm = distanceKm.setScale(2, RoundingMode.HALF_UP),
            totalCost = totalCost.setScale(2, RoundingMode.HALF_UP),
            fuelConsumption = fuelConsumption.setScale(2, RoundingMode.HALF_UP),
            fuelEfficiency = fuelEfficiency.setScale(2, RoundingMode.HALF_UP),
            pricePerLiter = pricePerLiter.setScale(2, RoundingMode.HALF_UP),
            costPerKilometer = costPerKilometer.setScale(3, RoundingMode.HALF_UP),
            costPer100Km = costPer100Km.setScale(2, RoundingMode.HALF_UP),
        )
    }
}
