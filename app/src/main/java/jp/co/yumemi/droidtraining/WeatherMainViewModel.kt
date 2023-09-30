package jp.co.yumemi.droidtraining

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.droidtraining.usecases.GetWeatherInfoDataUseCase
import jp.co.yumemi.droidtraining.usecases.UpdateWeatherInfoDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class WeatherMainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val updateWeatherInfoDataUseCase: UpdateWeatherInfoDataUseCase,
    val getWeatherInfoDataUseCase: GetWeatherInfoDataUseCase,
) : ViewModel() {
    private val isShowErrorDialogKey = "isShowErrorDialog"
    private val _isShowErrorDialog = MutableStateFlow(
        savedStateHandle.get<Boolean>(isShowErrorDialogKey) ?: false,
    )
    val isShowErrorDialog = _isShowErrorDialog.asStateFlow()

    val weatherInfoData = getWeatherInfoDataUseCase()
    private val _updating = MutableStateFlow(false)
    val updating = _updating.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                isShowErrorDialog.collect { savedStateHandle[isShowErrorDialogKey] = it }
            }
        }
    }

    fun reloadWeather() {
        viewModelScope.launch {
            _updating.value = true
            updateWeatherInfoDataUseCase(onFailed = {
                showErrorDialog()
            })
            _updating.value = false
        }
    }

    private fun showErrorDialog() {
        _isShowErrorDialog.value = true
    }

    fun closeErrorDialog() {
        _isShowErrorDialog.value = false
    }
}
