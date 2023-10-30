package jp.co.yumemi.droidtraining.usecases

import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import javax.inject.Inject

class GetForecastWeatherInfoDataListUseCase @Inject constructor(
    private val weatherInfoDataRepository: WeatherInfoDataRepository,
) {
    operator fun invoke() = weatherInfoDataRepository.forecastWeatherInfoDataList
}
