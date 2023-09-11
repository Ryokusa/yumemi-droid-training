package jp.co.yumemi.droidtraining.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import jp.co.yumemi.droidtraining.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import java.io.IOException

class CurrentWeatherDataAPI {
    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val API_KEY = BuildConfig.API_KEY

        enum class CityId(val id: Int) {
            SAPPORO(2128295),
            KUSIRO(2129376),
            TOKYO(1850144),
            NAGOYA(1856057),
        }
    }

    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    private val currentWeatherDataService = retrofit.create(CurrentWeatherDataService::class.java)


    suspend fun fetchCurrentWeatherData(cityId: CityId): CurrentWeatherData {
        try {
            val currentWeatherDataResponse = currentWeatherDataService
                .fetchCurrentWeatherData(API_KEY, cityId.id)
            if (currentWeatherDataResponse.isSuccessful) {
                return currentWeatherDataResponse.body()
                    ?: throw IOException("天気情報を取得できませんでした")
            }
            throw IOException("天気情報を取得できませんでした")
        } catch (e: Throwable) {
            throw e
        }
    }
}
