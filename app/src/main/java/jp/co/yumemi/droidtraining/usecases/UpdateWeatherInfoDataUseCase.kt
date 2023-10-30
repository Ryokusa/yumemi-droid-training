package jp.co.yumemi.droidtraining.usecases

import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class UpdateWeatherInfoDataUseCase @Inject constructor(private val weatherInfoDataRepository: WeatherInfoDataRepository) {
    suspend operator fun invoke(onFailed: () -> Unit, onCancel: () -> Unit) {
        try {
            weatherInfoDataRepository.updateWeatherInfoData()
        } catch (e: CancellationException) {
            onCancel()
        } catch (e: UnknownException) {
            onFailed()
        }
    }
}
