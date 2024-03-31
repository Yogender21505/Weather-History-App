package com.Yogender.weatherapp.models

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import java.util.Date


@Dao
interface WeatherDao {
    @Upsert
    fun insertWeather(weather: Weather)

    @Query("SELECT * FROM Weather")
    fun getAlDatabase(): List<Weather>

    @Query("SELECT * FROM Weather WHERE latitude = :latitude AND longitude = :longitude AND time = :date")
    fun getWeather(date: String,latitude: Double,longitude: Double): Weather

    @Query("SELECT COUNT(*) FROM Weather WHERE strftime('%Y', time) >= strftime('%Y', '2023-01-01', '-10 years') AND latitude = :latitude AND longitude = :longitude")
    fun getlast10yearWeather(latitude: Double,longitude: Double): Int


    @Query("SELECT temperature_2m_min FROM Weather WHERE latitude = :latitude AND longitude = :longitude AND time = :date")
    fun getMinTemperatureForTime(date: String,latitude: Double,longitude: Double): Double?

    @Query("SELECT temperature_2m_max FROM Weather WHERE latitude = :latitude AND longitude = :longitude AND time = :date")
    fun getMaxTemperatureForTime(date: String,latitude: Double,longitude: Double): Double?

    @Query("SELECT AVG(temperature_2m_min) FROM Weather WHERE strftime('%Y', time) >= strftime('%Y', '2023-01-01', '-10 years') AND latitude = :latitude AND longitude = :longitude")
    fun getAverageMinTemperatureLast10Years(latitude: Double, longitude: Double): Double?

    @Query("SELECT AVG(temperature_2m_max) FROM Weather WHERE strftime('%Y', time) >= strftime('%Y', '2023-01-01', '-10 years') AND latitude = :latitude AND longitude = :longitude")
    fun getAverageMaxTemperatureLast10Years(latitude: Double,longitude: Double): Double?
}
