package com.example.fueltracker

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class FuelCalculatorTest {

    @Test
    fun `calculates expected values for miles input`() {
        val result = FuelCalculator.calculate(
            litersText = "56.80",
            totalPriceText = "92.00",
            distanceText = "346.9",
            distanceUnit = DistanceUnit.MILES,
        )

        assertEquals(BigDecimal("558.25"), result.distanceKm)
        assertEquals(BigDecimal("10.17"), result.fuelConsumption)
        assertEquals(BigDecimal("9.83"), result.fuelEfficiency)
        assertEquals(BigDecimal("1.62"), result.pricePerLiter)
        assertEquals(BigDecimal("0.165"), result.costPerKilometer)
        assertEquals(BigDecimal("16.48"), result.costPer100Km)
    }

    @Test
    fun `converts miles to kilometers`() {
        val converted = FuelCalculator.convertDistanceToKilometers(BigDecimal("10"), DistanceUnit.MILES)
        assertEquals(BigDecimal("16.09"), converted.setScale(2, RoundingMode.HALF_UP))
    }

    @Test
    fun `rejects zero distance`() {
        val result = FuelCalculator.calculate(
            litersText = "50",
            totalPriceText = "70",
            distanceText = "0",
            distanceUnit = DistanceUnit.KILOMETERS,
        )
        assertEquals("Distance must be greater than zero.", result.errorMessage)
    }

    @Test
    fun `rejects negative distance`() {
        val result = FuelCalculator.calculate(
            litersText = "50",
            totalPriceText = "70",
            distanceText = "-1",
            distanceUnit = DistanceUnit.KILOMETERS,
        )
        assertEquals("Distance must be greater than zero.", result.errorMessage)
    }

    @Test
    fun `rejects zero fuel volume`() {
        val result = FuelCalculator.calculate(
            litersText = "0",
            totalPriceText = "70",
            distanceText = "100",
            distanceUnit = DistanceUnit.KILOMETERS,
        )
        assertEquals("Fuel volume must be greater than zero.", result.errorMessage)
    }

    @Test
    fun `rejects negative fuel volume`() {
        val result = FuelCalculator.calculate(
            litersText = "-5",
            totalPriceText = "70",
            distanceText = "100",
            distanceUnit = DistanceUnit.KILOMETERS,
        )
        assertEquals("Fuel volume must be greater than zero.", result.errorMessage)
    }

    @Test
    fun `rejects invalid numeric input`() {
        val result = FuelCalculator.calculate(
            litersText = "abc",
            totalPriceText = "92.00",
            distanceText = "150",
            distanceUnit = DistanceUnit.KILOMETERS,
        )
        assertEquals("Please enter valid numeric values.", result.errorMessage)
    }
}
