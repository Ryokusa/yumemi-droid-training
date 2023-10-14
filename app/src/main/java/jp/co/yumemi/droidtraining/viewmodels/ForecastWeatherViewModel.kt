package jp.co.yumemi.droidtraining.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.droidtraining.usecases.GetForecastWeatherInfoDataListUseCase
import jp.co.yumemi.droidtraining.usecases.UpdateForecastWeatherInfoDataListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ForecastWeatherViewModel @Inject constructor(
    getForecastWeatherInfoDataListUseCase: GetForecastWeatherInfoDataListUseCase,
    private val updateForecastWeatherInfoDataListUseCase: UpdateForecastWeatherInfoDataListUseCase,
) : ViewModel() {

    val forecastWeatherInfoDataList = getForecastWeatherInfoDataListUseCase()
    private val _forecastFetching = MutableStateFlow(false)
    val forecastFetching = _forecastFetching.asStateFlow()

    private var fetchForecastWeatherJob: Job? = null

    open fun fetchForecastWeather(onFailed: () -> Unit) {
        fetchForecastWeatherJob = viewModelScope.launch {
            _forecastFetching.value = true
            updateForecastWeatherInfoDataListUseCase(
                onFailed = {
                    onFailed()
                },
                onCancel = {},
            )
            _forecastFetching.value = false
            onFailed()
        }
    }

    open fun cancelFetchForecastWeather() {
        fetchForecastWeatherJob?.cancel()
    }
}
