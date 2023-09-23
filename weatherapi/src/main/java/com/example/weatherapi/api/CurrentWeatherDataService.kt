package com.example.weatherapi.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentWeatherDataService {
    @GET("weather")
    suspend fun fetchCurrentWeatherData(
        @Query("appid") apiKey: String,
        @Query("id") cityId: Int,
    ): Response<CurrentWeatherData>
}
