package com.example.fueltracker

import org.junit.Assert.assertEquals
import org.junit.Test

class FuelCalculatorTest {

    @Test
    fun `calculates expected values for a top up`() {
        val result = FuelCalculator.fromTopUp(
            liters = 60.0,
            totalCost = 99.0,
            miles = 430.0,
        )

        assertEquals(60.0, result.liters, 0.0001)
        assertEquals(99.0, result.totalCost, 0.0001)
        assertEquals(430.0, result.milesSinceLastRefill, 0.0001)
        assertEquals(1.65, result.pricePerLiter, 0.0001)
        assertEquals(13.95, FuelCalculator.round2(result.consumptionLPer100Mi), 0.0001)
        assertEquals(7.17, FuelCalculator.round2(result.efficiencyMiPerL), 0.0001)
        assertEquals(0.23, FuelCalculator.round2(result.costPerMile), 0.0001)
    }

    @Test
    fun `calculates quick calculator result for kilometers`() {
        val result = FuelCalculator.calculate(
            litersText = "60",
            pricePerLiterText = "1.65",
            distanceText = "430",
            distanceUnit = DistanceUnit.KILOMETERS,
        )

        assertEquals("Please enter valid numeric values.", null)
        assertEquals(60.0, result.totalCost?.toDouble() ?: 0.0, 0.0001)
    }
}
