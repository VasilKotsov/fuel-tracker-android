package com.example.fueltracker

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class FuelCalculatorTest {

    @Test
    fun `converts distance from miles to kilometers`() {
        val result = FuelCalculator.convertDistanceToKilometers(BigDecimal("466"), DistanceUnit.MILES)
        assertEquals(BigDecimal("750.01"), result.setScale(2, java.math.RoundingMode.HALF_UP))
    }

    @Test
    fun `converts distance from kilometers to miles`() {
        val result = FuelCalculator.convertDistanceToMiles(BigDecimal("750.01"))
        assertEquals(BigDecimal("466.00"), result.setScale(2, java.math.RoundingMode.HALF_UP))
    }

    @Test
    fun `converts fuel consumption from per 100km to per 100 miles`() {
        val result = FuelCalculator.convertConsumptionPer100KmToPer100Miles(BigDecimal("8.13"))
        assertEquals(BigDecimal("13.09"), result.setScale(2, java.math.RoundingMode.HALF_UP))
    }

    @Test
    fun `converts fuel efficiency from km per liter to miles per liter`() {
        val result = FuelCalculator.convertEfficiencyKmPerLiterToMilesPerLiter(BigDecimal("12.30"))
        assertEquals(BigDecimal("7.64"), result.setScale(2, java.math.RoundingMode.HALF_UP))
    }

    @Test
    fun `converts cost per kilometer to cost per mile`() {
        val result = FuelCalculator.convertCostPerKmToCostPerMile(BigDecimal("0.131"))
        assertEquals(BigDecimal("0.21"), result.setScale(2, java.math.RoundingMode.HALF_UP))
    }

    @Test
    fun `keeps total cost unchanged when switching units`() {
        val result = FuelCalculator.calculate(
            litersText = "61",
            pricePerLiterText = "1.61",
            distanceText = "466",
            distanceUnit = DistanceUnit.MILES,
        )

        assertEquals(BigDecimal("98.21"), result.totalCost)
    }

    @Test
    fun `rejects zero distance`() {
        val result = FuelCalculator.calculate(
            litersText = "50",
            pricePerLiterText = "1.50",
            distanceText = "0",
            distanceUnit = DistanceUnit.KILOMETERS,
        )
        assertEquals("Distance must be greater than zero.", result.errorMessage)
    }

    @Test
    fun `rejects invalid numeric input`() {
        val result = FuelCalculator.calculate(
            litersText = "abc",
            pricePerLiterText = "1.50",
            distanceText = "100",
            distanceUnit = DistanceUnit.KILOMETERS,
        )
        assertEquals("Please enter valid numeric values.", result.errorMessage)
    }
}
