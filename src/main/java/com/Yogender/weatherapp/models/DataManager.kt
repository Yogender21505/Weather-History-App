package com.Yogender.weatherapp.models

import android.content.Context

import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

object DataManager {
    var countries: Map<String, Pair<Double, Double>> = emptyMap()
    var countryfromlatilongi: Map<Pair<Double,Double>,String> = emptyMap()
    var list: MutableList<String> = mutableListOf()
    fun loadAsset(context: Context) {
        try {
            val inputStream: InputStream = context.assets.open("countryname.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val json = String(buffer, Charset.forName("UTF-8"))
            val gson = Gson()

            // Parse JSON array into a list of CountryData objects
            val countryDataList = gson.fromJson(json, Array<CountryData>::class.java).toList()

            // Convert the list to a map of country names to latitude-longitude pairs
            countries = countryDataList.associate { it.country_name to Pair(it.latitude, it.longitude) }
            countryfromlatilongi = countryDataList.associate {  Pair(it.latitude, it.longitude) to it.country_name }
            list.addAll(countryfromlatilongi.values)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
