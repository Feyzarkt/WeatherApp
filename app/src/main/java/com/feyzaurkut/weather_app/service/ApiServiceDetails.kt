package com.feyzaurkut.weather_app.service

import com.feyzaurkut.weather_app.model.WeatherResponseDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceDetails {

    @GET("api/location/{woeid}/")
    suspend fun listWeatherDetails(@Path("woeid") woeid:String): Response<WeatherResponseDetails>


}