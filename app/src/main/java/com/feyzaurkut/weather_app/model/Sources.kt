package com.feyzaurkut.weather_app.model

import com.google.gson.annotations.SerializedName

data class Sources (

        @SerializedName("title") val title : String,
        @SerializedName("slug") val slug : String,
        @SerializedName("url") val url : String,
        @SerializedName("crawl_rate") val crawl_rate : Int
)