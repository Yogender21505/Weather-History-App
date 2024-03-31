package com.Yogender.weatherapp.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(
    val latitude: Double,
    val longitude: Double,
    val time: String,

    @Embedded
    val temp: Temp,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

data class Temp(
    val temperature_2m_max: Double,
    val temperature_2m_min: Double
)