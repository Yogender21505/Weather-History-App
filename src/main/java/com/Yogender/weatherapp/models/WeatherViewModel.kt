package com.Yogender.weatherapp.models

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel(private val dao: WeatherDao):ViewModel() {
    var MINtemp = mutableStateOf(0.0)
    var MAXtemp = mutableStateOf(0.0)
    var loadingProgress =mutableStateOf(0f)
    val isLoading =  mutableStateOf(false)
    val isToast = mutableStateOf(false)
    val error = mutableStateOf("")
    val databaseList: MutableSet<Pair<Pair<String, String>, Pair<Double, Double>>> = mutableSetOf()

    fun updateMinMax(
        date: String,
        latitude: Double,
        longitude: Double,
        context: Context,
        toast: MutableState<Boolean>
    ) {
        viewModelScope.launch {
            try {
//                println("hello")
                val repository = WeatherRepository(dao)
                repository.getWeatherData(date,latitude,longitude,MINtemp,MAXtemp,loadingProgress,isLoading, context,isToast)
                repository.getDatabase(databaseList)
            } catch (e: Exception) {
//                // Handle error
//                println("internet required")
//                println(e)
            }
        }
    }
    fun getDatabaseData(
    ) {
        viewModelScope.launch {
            try {
                val repository = WeatherRepository(dao)
                repository.getDatabase(databaseList)
            } catch (e: Exception) {
                error("Empty Database")
            }
        }
    }

}