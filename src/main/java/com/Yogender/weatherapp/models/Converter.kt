package com.Yogender.weatherapp.models

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun fromDoubleList(list: List<Double>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun toDoubleList(value: String?): List<Double>? {
        return value?.split(",")?.map { it.toDouble() }
    }

}