package com.feyzaurkut.weather_app.model

import com.google.gson.annotations.SerializedName

data class WeatherResponseDetails (

    @SerializedName("consolidated_weather") val consolidated_weather : List<Consolidated_weather>,
    @SerializedName("time") val time : String,
    @SerializedName("sun_rise") val sun_rise : String,
    @SerializedName("sun_set") val sun_set : String,
    @SerializedName("timezone_name") val timezone_name : String,
    @SerializedName("parent") val parent : Parent,
    @SerializedName("sources") val sources : List<Sources>,
    @SerializedName("title") val title : String,
    @SerializedName("location_type") val location_type : String,
    @SerializedName("woeid") val woeid : Int,
    @SerializedName("latt_long") val latt_long : String,
    @SerializedName("timezone") val timezone : String
)