package jp.co.yumemi.droidtraining.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.droidtraining.usecases.GetForecastWeatherInfoDataListUseCase
import jp.co.yumemi.droidtraining.usecases.GetWeatherInfoDataUseCase
import jp.co.yumemi.droidtraining.usecases.UpdateForecastWeatherInfoDataListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ForecastWeatherViewModel @Inject constructor(
    getWeatherInfoDataUseCase: GetWeatherInfoDataUseCase,
    getForecastWeatherInfoDataListUseCase: GetForecastWeatherInfoDataListUseCase,
    private val updateForecastWeatherInfoDataListUseCase: UpdateForecastWeatherInfoDataListUseCase,
) : ViewModel() {

    val forecastWeatherInfoDataList = getForecastWeatherInfoDataListUseCase()
    private val _forecastFetching = MutableStateFlow(false)
    val forecastFetching = _forecastFetching.asStateFlow()

    val weatherInfoData = getWeatherInfoDataUseCase()

    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog = _showErrorDialog.asStateFlow()

    private var fetchForecastWeatherJob: Job? = null

    fun closeErrorDialog() {
        _showErrorDialog.value = false
    }

    fun showErrorDialog() {
        _showErrorDialog.value = true
    }

    open fun fetchForecastWeather() {
        fetchForecastWeatherJob = viewModelScope.launch {
            _forecastFetching.value = true
            updateForecastWeatherInfoDataListUseCase(
                onFailed = {
                    _showErrorDialog.value = true
                },
                onCancel = {},
            )
            _forecastFetching.value = false
        }
    }

    open fun cancelFetchForecastWeather() {
        fetchForecastWeatherJob?.cancel()
    }
}
