package com.example.fueltracker

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

private val ONE_HUNDRED = BigDecimal("100")
private val MILES_TO_KM = BigDecimal("1.609344")
private val ZERO = BigDecimal.ZERO

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

        if (liters <= ZERO) {
            return FuelCalculationResult(errorMessage = "Fuel volume must be greater than zero.")
        }

        if (pricePerLiter < ZERO) {
            return FuelCalculationResult(errorMessage = "Price per liter cannot be negative.")
        }

        if (distance <= ZERO) {
            return FuelCalculationResult(errorMessage = "Distance must be greater than zero.")
        }

        val distanceKm = convertDistanceToKilometers(distance, distanceUnit)
        val totalCost = liters.multiply(pricePerLiter)
        val fuelConsumption = liters.divide(distanceKm, 6, RoundingMode.HALF_UP)
            .multiply(ONE_HUNDRED)
        val fuelEfficiency = distanceKm.divide(liters, 6, RoundingMode.HALF_UP)
        val costPerKilometer = totalCost.divide(distanceKm, 6, RoundingMode.HALF_UP)
        val costPer100Km = costPerKilometer.multiply(ONE_HUNDRED)

        return FuelCalculationResult(
            distanceKm = distanceKm,
            totalCost = totalCost,
            fuelConsumption = fuelConsumption,
            fuelEfficiency = fuelEfficiency,
            pricePerLiter = pricePerLiter,
            costPerKilometer = costPerKilometer,
            costPer100Km = costPer100Km,
        )
    }

    fun convertDistanceToKilometers(distance: BigDecimal, unit: DistanceUnit): BigDecimal {
        return when (unit) {
            DistanceUnit.KILOMETERS -> distance
            DistanceUnit.MILES -> distance.multiply(MILES_TO_KM)
        }
    }

    fun convertDistanceToMiles(distanceKm: BigDecimal): BigDecimal {
        return distanceKm.divide(MILES_TO_KM, 6, RoundingMode.HALF_UP)
    }

    fun convertConsumptionPer100KmToPer100Miles(consumptionPer100Km: BigDecimal): BigDecimal {
        return consumptionPer100Km.multiply(BigDecimal("1.609344"))
    }

    fun convertEfficiencyKmPerLiterToMilesPerLiter(efficiencyKmPerLiter: BigDecimal): BigDecimal {
        return efficiencyKmPerLiter.divide(MILES_TO_KM, 6, RoundingMode.HALF_UP)
    }

    fun convertCostPerKmToCostPerMile(costPerKm: BigDecimal): BigDecimal {
        return costPerKm.divide(MILES_TO_KM, 6, RoundingMode.HALF_UP)
    }

    fun convertCostPer100KmToCostPer100Miles(costPer100Km: BigDecimal): BigDecimal {
        return costPer100Km.multiply(BigDecimal("1.609344"))
    }

    fun fromTopUp(
        liters: Double,
        totalCost: Double,
        miles: Double,
    ): FuelRecord {
        val safeMiles = if (miles <= 0.0) 1.0 else miles
        val safeLiters = if (liters <= 0.0) 1.0 else liters

        val pricePerLiter = totalCost / safeLiters
        val consumptionLPer100Mi = (safeLiters / safeMiles) * 100.0
        val efficiencyMiPerL = safeMiles / safeLiters
        val costPerMile = totalCost / safeMiles
        val distanceKm = safeMiles * 1.609344

        return FuelRecord(
            liters = liters,
            totalCost = totalCost,
            distanceMiles = miles,
            distanceKm = distanceKm,
            milesSinceLastRefill = miles,
            pricePerLiter = pricePerLiter,
            consumptionLPer100Mi = consumptionLPer100Mi,
            consumptionLPer100Km = (safeLiters / distanceKm) * 100.0,
            efficiencyMiPerL = efficiencyMiPerL,
            efficiencyKmPerL = distanceKm / safeLiters,
            costPerMile = costPerMile,
            costPerKm = totalCost / distanceKm,
        )
    }

    fun round2(value: Double): Double {
        return BigDecimal(value.toString())
            .setScale(2, RoundingMode.HALF_UP)
            .toDouble()
    }
}
