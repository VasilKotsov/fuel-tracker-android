package com.example.fueltracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fueltracker.ui.theme.FuelTrackerTheme
import java.math.BigDecimal
import java.math.RoundingMode

private val ONE_HUNDRED = BigDecimal("100")

@Composable
fun FuelTrackerApp() {
    var litersText by rememberSaveable { mutableStateOf("56.80") }
    var pricePerLiterText by rememberSaveable { mutableStateOf("1.62") }
    var distanceText by rememberSaveable { mutableStateOf("346.9") }
    var distanceUnit by rememberSaveable { mutableStateOf(DistanceUnit.KILOMETERS) }

    val calculation = FuelCalculator.calculate(
        litersText = litersText,
        pricePerLiterText = pricePerLiterText,
        distanceText = distanceText,
        distanceUnit = distanceUnit,
    )

    val liters = litersText.toBigDecimalOrNull() ?: BigDecimal.ZERO
    val distanceInput = distanceText.toBigDecimalOrNull() ?: BigDecimal.ZERO
    val distanceKm = FuelCalculator.convertDistanceToKilometers(distanceInput, distanceUnit)

    val primaryResults: List<String> = if (distanceUnit == DistanceUnit.KILOMETERS) {
        listOf(
            "Distance: ${formatDecimal(calculation.distanceKm)} km",
            "Total cost: €${formatDecimal(calculation.totalCost)}",
            "Consumption: ${formatDecimal(calculation.fuelConsumption)} L/100 km",
            "Efficiency: ${formatDecimal(calculation.fuelEfficiency)} km/L",
            "Price per liter: €${formatDecimal(calculation.pricePerLiter)}",
            "Cost/km: €${formatDecimal(calculation.costPerKilometer, 3)}",
            "Cost/100 km: €${formatDecimal(calculation.costPer100Km)}",
        )
    } else {
        val milesConsumption = liters.divide(distanceInput, 6, RoundingMode.HALF_UP)
            .multiply(ONE_HUNDRED)
        val milesEfficiency = distanceInput.divide(liters, 6, RoundingMode.HALF_UP)
        val milesCostPerDistance = (calculation.totalCost ?: BigDecimal.ZERO)
            .divide(distanceInput, 6, RoundingMode.HALF_UP)
        val milesCostPer100 = milesCostPerDistance.multiply(ONE_HUNDRED)

        listOf(
            "Distance: ${formatDecimal(distanceInput)} mi",
            "Total cost: €${formatDecimal(calculation.totalCost)}",
            "Consumption: ${formatDecimal(milesConsumption)} L/100 mi",
            "Efficiency: ${formatDecimal(milesEfficiency)} mi/L",
            "Price per liter: €${formatDecimal(calculation.pricePerLiter)}",
            "Cost/mile: €${formatDecimal(milesCostPerDistance, 3)}",
            "Cost/100 miles: €${formatDecimal(milesCostPer100)}",
        )
    }

    val convertedResults: List<String> = if (distanceUnit == DistanceUnit.KILOMETERS) {
        val milesDistance = FuelCalculator.convertDistanceToMiles(distanceKm)
        val milesConsumption = FuelCalculator.convertConsumptionPer100KmToPer100Miles(
            calculation.fuelConsumption ?: BigDecimal.ZERO,
        )
        val milesEfficiency = FuelCalculator.convertEfficiencyKmPerLiterToMilesPerLiter(
            calculation.fuelEfficiency ?: BigDecimal.ZERO,
        )
        val milesCostPerDistance = FuelCalculator.convertCostPerKmToCostPerMile(
            calculation.costPerKilometer ?: BigDecimal.ZERO,
        )
        val milesCostPer100 = FuelCalculator.convertCostPer100KmToCostPer100Miles(
            calculation.costPer100Km ?: BigDecimal.ZERO,
        )

        listOf(
            "Distance: ${formatDecimal(milesDistance)} mi",
            "Consumption: ${formatDecimal(milesConsumption)} L/100 mi",
            "Efficiency: ${formatDecimal(milesEfficiency)} mi/L",
            "Cost/mile: €${formatDecimal(milesCostPerDistance, 3)}",
            "Cost/100 miles: €${formatDecimal(milesCostPer100)}",
        )
    } else {
        val kmDistance = FuelCalculator.convertDistanceToKilometers(distanceInput, DistanceUnit.MILES)
        val kmConsumption = liters.divide(kmDistance, 6, RoundingMode.HALF_UP)
            .multiply(ONE_HUNDRED)
        val kmEfficiency = kmDistance.divide(liters, 6, RoundingMode.HALF_UP)
        val kmCostPerDistance = (calculation.totalCost ?: BigDecimal.ZERO)
            .divide(kmDistance, 6, RoundingMode.HALF_UP)
        val kmCostPer100 = kmCostPerDistance.multiply(ONE_HUNDRED)

        listOf(
            "Distance: ${formatDecimal(kmDistance)} km",
            "Consumption: ${formatDecimal(kmConsumption)} L/100 km",
            "Efficiency: ${formatDecimal(kmEfficiency)} km/L",
            "Cost/km: €${formatDecimal(kmCostPerDistance, 3)}",
            "Cost/100 km: €${formatDecimal(kmCostPer100)}",
        )
    }

    FuelTrackerTheme {
        Scaffold { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "Fuel Tracker",
                        style = MaterialTheme.typography.headlineMedium,
                    )

                    OutlinedTextField(
                        value = litersText,
                        onValueChange = { litersText = it },
                        label = { Text("Fuel volume (L)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )

                    OutlinedTextField(
                        value = pricePerLiterText,
                        onValueChange = { pricePerLiterText = it },
                        label = { Text("Price per liter (€)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )

                    OutlinedTextField(
                        value = distanceText,
                        onValueChange = { distanceText = it },
                        label = { Text("Distance") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        UnitToggleButton(
                            label = "km",
                            selected = distanceUnit == DistanceUnit.KILOMETERS,
                            onClick = { distanceUnit = DistanceUnit.KILOMETERS },
                            modifier = Modifier.weight(1f),
                        )
                        UnitToggleButton(
                            label = "mi",
                            selected = distanceUnit == DistanceUnit.MILES,
                            onClick = { distanceUnit = DistanceUnit.MILES },
                            modifier = Modifier.weight(1f),
                        )
                    }

                    if (calculation.errorMessage != null) {
                        Text(
                            text = calculation.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    } else {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text("Estimated result", style = MaterialTheme.typography.titleMedium)

                                primaryResults.forEach { text ->
                                    Text(text)
                                }

                                Text(
                                    text = if (distanceUnit == DistanceUnit.KILOMETERS) {
                                        "Converted results (miles)"
                                    } else {
                                        "Converted results (kilometers)"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                )

                                convertedResults.forEach { text ->
                                    Text(text)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UnitToggleButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selectedColor = Color(0xFF9AD6A5)
    val unselectedColor = Color(0xFF3A3A3C)
    val textColor = if (selected) Color(0xFF111111) else Color(0xFFEAEAEA)

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) selectedColor else unselectedColor,
            contentColor = textColor,
        ),
        shape = MaterialTheme.shapes.large,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

private fun formatDecimal(value: BigDecimal?, scale: Int = 2): String {
    if (value == null) return "-"
    return value.setScale(scale, RoundingMode.HALF_UP).toPlainString()
}
