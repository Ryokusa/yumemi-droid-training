package jp.co.yumemi.droidtraining

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.droidtraining.usecases.GetForecastWeatherInfoDataUseCase
import jp.co.yumemi.droidtraining.usecases.UpdateForecastWeatherInfoDataListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ForecastWeatherViewModel @Inject constructor(
    getForecastWeatherInfoDataUseCase: GetForecastWeatherInfoDataUseCase,
    private val updateForecastWeatherInfoDataListUseCase: UpdateForecastWeatherInfoDataListUseCase,
) : ViewModel() {

    val forecastWeatherInfoDataList = getForecastWeatherInfoDataUseCase()
    private val _forecastFetching = MutableStateFlow(false)
    val forecastFetching = _forecastFetching.asStateFlow()

    private var fetchForecastWeatherJob: Job? = null

    open fun fetchForecastWeather(onFailed: () -> Unit, onCancel: () -> Unit = {}) {
        fetchForecastWeatherJob = viewModelScope.launch {
            _forecastFetching.value = true
            updateForecastWeatherInfoDataListUseCase(
                onFailed = {
                    onFailed()
                },
                onCancel = {
                    onCancel()
                },
            )
            _forecastFetching.value = false
        }
    }

    open fun cancelFetchForecastWeather() {
        fetchForecastWeatherJob?.cancel()
    }
}
