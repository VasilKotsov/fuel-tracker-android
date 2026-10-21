package com.example.fueltracker

data class FuelRecord(
    val id: Long = 0L,
    val date: Long = System.currentTimeMillis(),
    val liters: Double,
    val totalCost: Double,
    val milesSinceLastRefill: Double,
) {
    val pricePerLiter: Double
        get() = if (liters > 0.0) totalCost / liters else 0.0
}
