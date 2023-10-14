package jp.co.yumemi.droidtraining.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.droidtraining.usecases.GetWeatherInfoDataUseCase
import jp.co.yumemi.droidtraining.usecases.UpdateWeatherInfoDataUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class WeatherMainViewModel @Inject constructor(
    val updateWeatherInfoDataUseCase: UpdateWeatherInfoDataUseCase,
    val getWeatherInfoDataUseCase: GetWeatherInfoDataUseCase,
) : ViewModel() {

    val weatherInfoData = getWeatherInfoDataUseCase()
    private val _updating = MutableStateFlow(false)
    val updating = _updating.asStateFlow()

    private var reloadWeatherJob: Job? = null

    fun reloadWeather(onFailed: () -> Unit, onCancel: () -> Unit = {}) {
        reloadWeatherJob = viewModelScope.launch {
            _updating.value = true
            updateWeatherInfoDataUseCase(
                onFailed = {
                    onFailed()
                },
                onCancel = {
                    onCancel()
                },
            )

            _updating.value = false
        }
    }

    fun cancelReloadWeather() {
        reloadWeatherJob?.cancel()
    }
}
