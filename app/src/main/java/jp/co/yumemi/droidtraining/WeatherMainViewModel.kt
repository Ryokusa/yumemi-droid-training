package jp.co.yumemi.droidtraining

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.droidtraining.usecases.GetForecastWeatherInfoDataUseCase
import jp.co.yumemi.droidtraining.usecases.GetWeatherInfoDataUseCase
import jp.co.yumemi.droidtraining.usecases.UpdateForecastWeatherInfoDataListUseCase
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
    val updateForecastWeatherInfoDataListUseCase: UpdateForecastWeatherInfoDataListUseCase,
    val getForecastWeatherInfoDataUseCase: GetForecastWeatherInfoDataUseCase,
) : ViewModel() {

    val weatherInfoData = getWeatherInfoDataUseCase()
    private val _updating = MutableStateFlow(false)
    val updating = _updating.asStateFlow()

    val forecastWeatherInfoDataList = getForecastWeatherInfoDataUseCase()
    private val _forecastFetching = MutableStateFlow(false)
    val forecastFetching = _forecastFetching.asStateFlow()

    private var reloadWeatherJob: Job? = null
    private var fetchForecastWeatherJob: Job? = null

    fun reloadWeather(onFailed: () -> Unit) {
        reloadWeatherJob = viewModelScope.launch {
            _updating.value = true
            updateWeatherInfoDataUseCase(onFailed = {
                onFailed()
            })
            _updating.value = false
        }
    }

    fun cancelReloadWeather() {
        reloadWeatherJob?.cancel()
    }

    fun fetchForecastWeather(onFailed: () -> Unit) {
        fetchForecastWeatherJob = viewModelScope.launch {
            _forecastFetching.value = true
            updateForecastWeatherInfoDataListUseCase(onFailed = {
                onFailed()
            })
            _forecastFetching.value = false
        }
    }

    fun cancelFetchForecastWeather() {
        fetchForecastWeatherJob?.cancel()
    }
}
