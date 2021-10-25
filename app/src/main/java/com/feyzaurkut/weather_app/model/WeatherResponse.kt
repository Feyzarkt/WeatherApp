package com.feyzaurkut.weather_app.model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse (
    val distance : Int,
    val title : String,
    val location_type : String,
    val woeid : Int,
    val latt_long : String
)

