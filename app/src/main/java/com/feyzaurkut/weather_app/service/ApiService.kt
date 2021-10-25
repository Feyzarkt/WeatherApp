package com.feyzaurkut.weather_app.service

import com.feyzaurkut.weather_app.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/location/search/?")
    suspend fun listWeather(@Query("lattlong") lattlong:String): Response<List<WeatherResponse>>

}


