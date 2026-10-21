package com.example.fueltracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fueltracker.ui.theme.FuelTrackerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun FuelTrackerApp() {
    val context = LocalContext.current
    val prefs = remember { FuelPreferences(context) }
    val records = remember { mutableStateListOf<FuelRecord>() }

    var litersText by remember { mutableStateOf("") }
    var totalCostText by remember { mutableStateOf("") }
    var milesText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        records.clear()
        records.addAll(prefs.loadRecords())
    }

    val latestSummary = remember(records) {
        if (records.isEmpty()) null else {
            val last = records.last()
            FuelCalculator.fromTopUp(
                liters = last.liters,
                totalCost = last.totalCost,
                miles = last.milesSinceLastRefill,
            )
        }
    }

    val totalMiles = records.sumOf { it.milesSinceLastRefill }
    val totalLiters = records.sumOf { it.liters }
    val totalCost = records.sumOf { it.totalCost }

    val averageConsumption = if (totalMiles > 0.0) {
        (totalLiters / totalMiles) * 100.0
    } else {
        0.0
    }

    val averageCostPerMile = if (totalMiles > 0.0) {
        totalCost / totalMiles
    } else {
        0.0
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
                        value = totalCostText,
                        onValueChange = { totalCostText = it },
                        label = { Text("Total cost (€)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )

                    OutlinedTextField(
                        value = milesText,
                        onValueChange = { milesText = it },
                        label = { Text("Miles since last refill") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )

                    Button(
                        onClick = {
                            val liters = litersText.toDoubleOrNull() ?: 0.0
                            val totalCostValue = totalCostText.toDoubleOrNull() ?: 0.0
                            val miles = milesText.toDoubleOrNull() ?: 0.0

                            if (liters > 0.0 && totalCostValue > 0.0 && miles > 0.0) {
                                val record = FuelRecord(
                                    id = System.currentTimeMillis(),
                                    date = System.currentTimeMillis(),
                                    liters = liters,
                                    totalCost = totalCostValue,
                                    milesSinceLastRefill = miles,
                                )

                                val updated = records.toMutableList()
                                updated.add(record)
                                records.clear()
                                records.addAll(updated)
                                prefs.saveRecords(updated)

                                litersText = ""
                                totalCostText = ""
                                milesText = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Save top up")
                    }

                    if (latestSummary != null) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                            ) {
                                Text(
                                    text = "Latest top up",
                                    style = MaterialTheme.typography.titleMedium,
                                )

                                Text("Liters: ${FuelCalculator.round2(latestSummary.liters)} L")
                                Text("Total cost: €${FuelCalculator.round2(latestSummary.totalCost)}")
                                Text("Miles: ${FuelCalculator.round2(latestSummary.miles)} mi")
                                Text("Price/liter: €${FuelCalculator.round2(latestSummary.pricePerLiter)}")
                                Text("Consumption: ${FuelCalculator.round2(latestSummary.consumptionLPer100Mi)} L/100 mi")
                                Text("Efficiency: ${FuelCalculator.round2(latestSummary.efficiencyMiPerL)} mi/L")
                                Text("Cost/mile: €${FuelCalculator.round2(latestSummary.costPerMile)}")
                            }
                        }
                    }

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            Text(
                                text = "Totals",
                                style = MaterialTheme.typography.titleMedium,
                            )

                            Text("Total miles: ${FuelCalculator.round2(totalMiles)} mi")
                            Text("Total liters: ${FuelCalculator.round2(totalLiters)} L")
                            Text("Total cost: €${FuelCalculator.round2(totalCost)}")
                            Text("Average consumption: ${FuelCalculator.round2(averageConsumption)} L/100 mi")
                            Text("Average cost/mile: €${FuelCalculator.round2(averageCostPerMile)}")
                        }
                    }

                    Text(
                        text = "History",
                        style = MaterialTheme.typography.titleMedium,
                    )

                    records.asReversed().forEach { record ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                                val dateText = sdf.format(Date(record.date))

                                Text(dateText)
                                Text("Liters: ${record.liters} L")
                                Text("Total cost: €${record.totalCost}")
                                Text("Miles: ${record.milesSinceLastRefill} mi")
                                Text("Price/liter: €${FuelCalculator.round2(record.pricePerLiter)}")
                            }
                        }
                    }
                }
            }
        }
    }
}
