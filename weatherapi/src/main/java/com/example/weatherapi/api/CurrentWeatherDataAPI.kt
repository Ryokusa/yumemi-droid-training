package com.example.weatherapi.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.IOException

private val contentType = "application/json".toMediaType()

interface CurrentWeatherDataAPI {

    enum class CityId(val id: Int) {
        SAPPORO(2128295),
        KUSIRO(2129376),
        TOKYO(1850144),
        NAGOYA(1856057),
    }

    companion object {

        operator fun invoke(
            apiKey: String,
            currentWeatherDataService: CurrentWeatherDataService? = null,
        ): CurrentWeatherDataAPI {
            if (currentWeatherDataService == null) {
                val client: OkHttpClient = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(JapaneseCurrentWeatherDataInterceptor())
                    .build()
                val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(MainCurrentWeatherDataAPI.BASE_URL)
                    .client(client)
                    .addConverterFactory(Json.asConverterFactory(contentType))
                    .build()
                return MainCurrentWeatherDataAPI(
                    apiKey,
                    retrofit.create(CurrentWeatherDataService::class.java),
                )
            }
            return MainCurrentWeatherDataAPI(apiKey, currentWeatherDataService)
        }
    }

    /**
     * 天気情報を取得
     * @args cityId 取得したい都市のID
     * @return 天気情報
     * @throws Exception 天気情報を取得できなかった場合
     */
    suspend fun fetchCurrentWeatherData(cityId: CityId): CurrentWeatherData

    suspend fun fetch5day3hourForecastData(cityId: CityId): ForecastDataList
}

class MainCurrentWeatherDataAPI(
    private val apiKey: String,
    private val currentWeatherDataService: CurrentWeatherDataService,
) : CurrentWeatherDataAPI {

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }

    override suspend fun fetchCurrentWeatherData(cityId: CurrentWeatherDataAPI.CityId): CurrentWeatherData {
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

    override suspend fun fetch5day3hourForecastData(cityId: CurrentWeatherDataAPI.CityId): ForecastDataList {
        try {
            val forecastDataListResponse = currentWeatherDataService
                .fetch5day3hourForecastData(apiKey, cityId.id)
            if (forecastDataListResponse.isSuccessful) {
                forecastDataListResponse.body()?.let {
                    return it
                }
            }
            throw IOException("天気情報を取得できませんでした")
        } catch (e: Throwable) {
            throw e
        }
    }
}
