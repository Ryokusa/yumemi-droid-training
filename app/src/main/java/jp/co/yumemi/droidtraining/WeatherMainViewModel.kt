package jp.co.yumemi.droidtraining

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.api.YumemiWeather
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import jp.co.yumemi.droidtraining.usecases.UpdateWeatherInfoDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class WeatherMainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val updateWeatherInfoDataUseCase: UpdateWeatherInfoDataUseCase
) : ViewModel() {
    private val isShowErrorDialogKey = "isShowErrorDialog"
    private val _isShowErrorDialog = MutableStateFlow(
        savedStateHandle.get<Boolean>(isShowErrorDialogKey) ?: false
    )
    val isShowErrorDialog = _isShowErrorDialog.asStateFlow()

    val weatherInfoData: StateFlow<WeatherInfoData> = updateWeatherInfoDataUseCase.weatherInfoData

    init {
        viewModelScope.launch {
            launch {
                isShowErrorDialog.collect { savedStateHandle[isShowErrorDialogKey] = it }
            }
        }
    }

    fun reloadWeather() {
        updateWeatherInfoDataUseCase.updateWeather(onFailed = {
            showErrorDialog()
        })
    }

    private fun showErrorDialog() {
        _isShowErrorDialog.value = true
    }

    fun closeErrorDialog() {
        _isShowErrorDialog.value = false
    }

}

class FakeWeatherMainViewModel(
    yumemiWeather: YumemiWeather,
    initialWeatherInfoData: WeatherInfoData
) : WeatherMainViewModel(
    updateWeatherInfoDataUseCase = UpdateWeatherInfoDataUseCase(
        WeatherInfoDataRepository(
            yumemiWeather,
            initialWeatherInfoData
        )
    ),
    savedStateHandle = SavedStateHandle()   //fake(empty)
)
