package com.example.fueltracker

import java.math.BigDecimal
import java.math.RoundingMode

data class FuelSummary(
    val liters: Double,
    val totalCost: Double,
    val miles: Double,
    val pricePerLiter: Double,
    val consumptionLPer100Mi: Double,
    val efficiencyMiPerL: Double,
    val costPerMile: Double,
)

object FuelCalculator {
    fun fromTopUp(
        liters: Double,
        totalCost: Double,
        miles: Double,
    ): FuelSummary {
        val safeMiles = if (miles <= 0.0) 1.0 else miles
        val safeLiters = if (liters <= 0.0) 1.0 else liters

        val pricePerLiter = totalCost / safeLiters
        val consumptionLPer100Mi = (safeLiters / safeMiles) * 100.0
        val efficiencyMiPerL = safeMiles / safeLiters
        val costPerMile = totalCost / safeMiles

        return FuelSummary(
            liters = liters,
            totalCost = totalCost,
            miles = miles,
            pricePerLiter = pricePerLiter,
            consumptionLPer100Mi = consumptionLPer100Mi,
            efficiencyMiPerL = efficiencyMiPerL,
            costPerMile = costPerMile,
        )
    }

    fun round2(value: Double): Double {
        return BigDecimal(value.toString())
            .setScale(2, RoundingMode.HALF_UP)
            .toDouble()
    }
}
