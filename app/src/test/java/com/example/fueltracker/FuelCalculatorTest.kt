package com.example.fueltracker

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class FuelCalculatorTest {

    @Test
    fun `calculates expected values for miles input with price per liter`() {
        val result = FuelCalculator.calculate(
            litersText = "60",
            pricePerLiterText = "1.65",
            distanceText = "430",
            distanceUnit = DistanceUnit.MILES,
        )

        assertEquals(BigDecimal("692.02"), result.distanceKm)
        assertEquals(BigDecimal("99.00"), result.totalCost)
        assertEquals(BigDecimal("8.67"), result.fuelConsumption)
        assertEquals(BigDecimal("11.53"), result.fuelEfficiency)
        assertEquals(BigDecimal("1.65"), result.pricePerLiter)
        assertEquals(BigDecimal("0.143"), result.costPerKilometer)
        assertEquals(BigDecimal("14.31"), result.costPer100Km)
    }

    @Test
    fun `converts miles to kilometers using exact factor`() {
        val converted = FuelCalculator.convertDistanceToKilometers(BigDecimal("430"), DistanceUnit.MILES)
        assertEquals(BigDecimal("692.02"), converted.setScale(2, RoundingMode.HALF_UP))
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
    fun `rejects negative price per liter`() {
        val result = FuelCalculator.calculate(
            litersText = "50",
            pricePerLiterText = "-1",
            distanceText = "100",
            distanceUnit = DistanceUnit.KILOMETERS,
        )
        assertEquals("Price per liter cannot be negative.", result.errorMessage)
    }

    @Test
    fun `rejects zero fuel volume`() {
        val result = FuelCalculator.calculate(
            litersText = "0",
            pricePerLiterText = "1.50",
            distanceText = "100",
            distanceUnit = DistanceUnit.KILOMETERS,
        )
        assertEquals("Fuel volume must be greater than zero.", result.errorMessage)
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
