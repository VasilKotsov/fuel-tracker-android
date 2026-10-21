package com.example.fueltracker

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FuelPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("fuel_tracker", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveRecords(records: List<FuelRecord>) {
        val json = gson.toJson(records)
        prefs.edit().putString("records", json).apply()
    }

    fun loadRecords(): List<FuelRecord> {
        val json = prefs.getString("records", null) ?: return emptyList()
        val type = object : TypeToken<List<FuelRecord>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}
