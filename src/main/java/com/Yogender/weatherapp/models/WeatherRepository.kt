package com.Yogender.weatherapp.models

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class WeatherRepository(private val weatherDao:WeatherDao) {
    suspend fun getMinMax(
        date: String,
        latitude: Double,
        longitude: Double,
        min: MutableState<Double>,
        max: MutableState<Double>,
        context: Context,
        isToast: MutableState<Boolean>,
        netOff: MutableState<Boolean>
    ) {
        withContext(Dispatchers.IO) {


                if(date>"2023-12-30"){

                        min.value = weatherDao.getAverageMinTemperatureLast10Years(latitude,longitude)!!
                        max.value = weatherDao.getAverageMaxTemperatureLast10Years(latitude,longitude)!!

                }
                else {

                    val minTemp = weatherDao.getMinTemperatureForTime(date, latitude, longitude)
                    val maxTemp = weatherDao.getMaxTemperatureForTime(date, latitude, longitude)
                    if (minTemp != null) {
                        min.value = minTemp
                    }
                    if (maxTemp != null) {
                        max.value = maxTemp
                    }
                }
            }
    }
    suspend fun getWeatherData(
        date: String,
        latitude: Double,
        longitude: Double,
        min: MutableState<Double>,
        max: MutableState<Double>,
        loadingProgress: MutableState<Float>,
        isLoading: MutableState<Boolean>,
        context: Context
        ,
        isToast: MutableState<Boolean>
    ) {
        var netOff = mutableStateOf(false)

        if (loadingProgress.value != (1).toFloat()) {
            isLoading.value = true
        }

        var weatherList = if (date > "2023-12-30") {

            println("hello")
                val apiUrl =
                    "https://archive-api.open-meteo.com/v1/archive?latitude=$latitude&longitude=$longitude&start_date=2013-01-01&end_date=2023-12-30&daily=temperature_2m_max,temperature_2m_min,rain_sum,wind_speed_10m_max,wind_gusts_10m_max,weather_code,shortwave_radiation_sum,precipitation_sum,wind_direction_10m_dominant"
                fetchWeatherDataFromApi(
                    apiUrl,
                    date,
                    context,
                    latitude,
                    longitude,
                    loadingProgress,
                    netOff
                ) // Pass context

        }
        else {
                val apiUrl =
                    "https://archive-api.open-meteo.com/v1/archive?latitude=$latitude&longitude=$longitude&start_date=$date&end_date=$date&daily=temperature_2m_max,temperature_2m_min,rain_sum,wind_speed_10m_max,wind_gusts_10m_max,weather_code,shortwave_radiation_sum,precipitation_sum,wind_direction_10m_dominant"
                fetchWeatherDataFromApi(
                    apiUrl,
                    date,
                    context,
                    latitude,
                    longitude,
                    loadingProgress,
                    netOff
                )
        }

        if (weatherList != null) {
            getMinMax(
                date,
                latitude,
                longitude,
                min,
                max,
                context,
                isToast,
                netOff
            )

        }
        else{
            if(date>"2023-12-30"){
                if(checkIspresent(latitude,longitude)){
                    getMinMax(
                        date,
                        latitude,
                        longitude,
                        min,
                        max,
                        context,
                        isToast,
                        netOff
                    )
                }
                else{
                    isToast.value=true
                }
            }
            else{
                if(checkIsdate(date,latitude,longitude)){
                    getMinMax(
                        date,
                        latitude,
                        longitude,
                        min,
                        max,
                        context,
                        isToast,
                        netOff
                    )
                }
                else{
                    isToast.value=true
                }
            }
        }


            if(isLoading.value==true){
                isLoading.value=false
                loadingProgress.value=0f
            }
            if (loadingProgress.value == (1).toFloat()) {
                isLoading.value = false
                loadingProgress.value=0f
            }

        println(min)
    }
    private suspend fun checkIsdate(
        date: String,
        latitude: Double,
        longitude: Double
    ):Boolean {
        return withContext(Dispatchers.IO) {
            if(weatherDao.getWeather(date,latitude, longitude)==null){
                false
            }
            else{
                true
            }
        }
    }
    private suspend fun checkIspresent(
        latitude: Double,
        longitude: Double
    ):Boolean {
        return withContext(Dispatchers.IO) {
            if(weatherDao.getlast10yearWeather(latitude, longitude) <10){
                false
            }
            else{
                true
            }
        }
    }

    private suspend fun fetchWeatherDataFromApi(
        api: String,
        date: String,
        context: Context, // Pass Context here
        latitude: Double,
        longitude: Double,
        loadingProgress: MutableState<Float>,
        netOff: MutableState<Boolean>
    ): MutableList<Temp>? {

        return withContext(Dispatchers.IO) {
            if (isNetworkAvailable(context)) {
                val url = URL(api)
                val connection = url.openConnection() as HttpsURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val jsonResponse = connection.inputStream.bufferedReader().use {
                    it.readText()
                }

                val jsonObject = JSONObject(jsonResponse)
                val dailyData = jsonObject.getJSONObject("daily")
                val timeArray = dailyData.getJSONArray("time")
                val temperatureArrayMax = dailyData.getJSONArray("temperature_2m_max")
                val temperatureArrayMin = dailyData.getJSONArray("temperature_2m_min")

                val dailyList = mutableListOf<Temp>()

                for (i in 0 until timeArray.length()) {
                    println(i)
                    val time = timeArray.getString(i)
                    val maxTemp = temperatureArrayMax.getDouble(i)
                    val minTemp = temperatureArrayMin.getDouble(i)
                    dailyList.add(Temp(maxTemp, minTemp))
                    weatherDao.insertWeather(Weather(
                        latitude = latitude,
                        longitude = longitude,
                        time = time,
                        temp = Temp(maxTemp, minTemp)
                    ))
                    // Calculate the progress as a percentage
                    val progress = (i + 1).toFloat() / timeArray.length() * 100
                    loadingProgress.value = progress
                    println(i)
//                    if(i==25){
//                        break
//                    }
                }
                dailyList // Return the populated list
            } else {
                netOff.value= true
                null // Return null indicating no data fetched
            }
        }
    }


    // Check internet connectivity status
    @SuppressLint("ServiceCast")
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    suspend fun getDatabase(databaseList: MutableSet<Pair<Pair<String, String>, Pair<Double, Double>>>) {
        withContext(Dispatchers.IO) {
            val weatherList: List<Weather> = weatherDao.getAlDatabase()

            weatherList.forEach { weather ->
                println(DataManager.countryfromlatilongi[Pair(weather.latitude, weather.longitude)] ?: "Unknown")
                val locationPair = Pair(DataManager.countryfromlatilongi[Pair(weather.latitude, weather.longitude)] ?: "Unknown",weather.time)
                val tempPair = Pair(weather.temp.temperature_2m_max, weather.temp.temperature_2m_min)
                val weatherPair = Pair(locationPair, tempPair)
                println(Pair(locationPair, tempPair))
                databaseList.add(weatherPair)
            }
        }
    }
}