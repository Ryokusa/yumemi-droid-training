package com.example.weatherapi.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.IOException

private val contentType = "application/json".toMediaType()

class CurrentWeatherDataAPI(
    private val apiKey: String,
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(JapaneseCurrentWeatherDataInterceptor())
        .build(),
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build(),
    private val currentWeatherDataService: CurrentWeatherDataService = retrofit.create(
        CurrentWeatherDataService::class.java
    )
) {
    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        enum class CityId(val id: Int) {
            SAPPORO(2128295),
            KUSIRO(2129376),
            TOKYO(1850144),
            NAGOYA(1856057),
        }
    }

    suspend fun fetchCurrentWeatherData(cityId: CityId): CurrentWeatherData {
        try {
            val currentWeatherDataResponse = currentWeatherDataService
                .fetchCurrentWeatherData(apiKey, cityId.id)
            if (currentWeatherDataResponse.isSuccessful) {
                currentWeatherDataResponse.body()?.let {
                    return it
                }
            }
            throw IOException("天気情報を取得できませんでした")
        } catch (e: Throwable) {
            throw e
        }
    }
}
