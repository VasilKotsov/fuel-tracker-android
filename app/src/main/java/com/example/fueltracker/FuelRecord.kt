package com.example.fueltracker

data class FuelRecord(
    val id: Long = 0L,
    val date: Long = System.currentTimeMillis(),
    val liters: Double,
    val totalCost: Double,
    val distanceMiles: Double = 0.0,
    val distanceKm: Double = 0.0,
    val milesSinceLastRefill: Double = 0.0,
    val pricePerLiter: Double = if (liters > 0.0) totalCost / liters else 0.0,
    val consumptionLPer100Mi: Double = 0.0,
    val consumptionLPer100Km: Double = 0.0,
    val efficiencyMiPerL: Double = 0.0,
    val efficiencyKmPerL: Double = 0.0,
    val costPerMile: Double = 0.0,
    val costPerKm: Double = 0.0,
)
