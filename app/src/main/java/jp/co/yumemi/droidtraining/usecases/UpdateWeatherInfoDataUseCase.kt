package jp.co.yumemi.droidtraining.usecases

import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import javax.inject.Inject

class UpdateWeatherInfoDataUseCase @Inject constructor(private val repository: WeatherInfoDataRepository) {
    val weatherInfoData = repository.weatherInfoData
    fun updateWeather(onFailed: () -> Unit) {
        try {
            repository.updateWeatherInfoData()
        }catch(e: UnknownException){
            onFailed()
        }
    }
}
