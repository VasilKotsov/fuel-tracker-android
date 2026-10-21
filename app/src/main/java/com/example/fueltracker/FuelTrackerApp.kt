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
import androidx.compose.ui.unit.dp
import com.example.fueltracker.ui.theme.FuelTrackerTheme
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun FuelTrackerApp() {
    var litersText by rememberSaveable { mutableStateOf("60") }
    var pricePerLiterText by rememberSaveable { mutableStateOf("1.65") }
    var distanceText by rememberSaveable { mutableStateOf("430") }
    var distanceUnit by rememberSaveable { mutableStateOf(DistanceUnit.MILES) }

    val calculation = FuelCalculator.calculate(
        litersText = litersText,
        pricePerLiterText = pricePerLiterText,
        distanceText = distanceText,
        distanceUnit = distanceUnit,
    )

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

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { distanceUnit = DistanceUnit.KILOMETERS },
                            enabled = distanceUnit != DistanceUnit.KILOMETERS,
                        ) {
                            Text("km")
                        }
                        Button(
                            onClick = { distanceUnit = DistanceUnit.MILES },
                            enabled = distanceUnit != DistanceUnit.MILES,
                        ) {
                            Text("mi")
                        }
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
                                Text("Distance: ${formatDecimal(calculation.distanceKm)} km")
                                Text("Total cost: €${formatDecimal(calculation.totalCost)}")
                                Text("Consumption: ${formatDecimal(calculation.fuelConsumption)} L/100 km")
                                Text("Efficiency: ${formatDecimal(calculation.fuelEfficiency)} km/L")
                                Text("Price per liter: €${formatDecimal(calculation.pricePerLiter)}")
                                Text("Cost/km: €${formatDecimal(calculation.costPerKilometer, 3)}")
                                Text("Cost/100 km: €${formatDecimal(calculation.costPer100Km)}")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatDecimal(value: BigDecimal?, scale: Int = 2): String {
    if (value == null) return "-"
    return value.setScale(scale, RoundingMode.HALF_UP).toPlainString()
}
