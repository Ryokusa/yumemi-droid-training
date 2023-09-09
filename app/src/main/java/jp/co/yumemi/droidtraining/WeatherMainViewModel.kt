package jp.co.yumemi.droidtraining

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.api.YumemiWeather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class WeatherMainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val yumemiWeather: YumemiWeather,
    initialWeatherInfoData: WeatherInfoData,
): ViewModel() {
    private val isShowErrorDialogKey = "isShowErrorDialog"
    private val _isShowErrorDialog = MutableStateFlow(
        savedStateHandle.get<Boolean>(isShowErrorDialogKey) ?: false
    )
    val isShowErrorDialog = _isShowErrorDialog.asStateFlow()

    private val weatherInfoDataKey = "weatherInfoData"
    private val _weatherInfoData = MutableStateFlow(
        savedStateHandle.get<WeatherInfoData>(weatherInfoDataKey) ?: initialWeatherInfoData
    )
    val weatherInfoData = _weatherInfoData.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                weatherInfoData.collect{ savedStateHandle[weatherInfoDataKey] = it }
            }
            launch {
                isShowErrorDialog.collect { savedStateHandle[isShowErrorDialogKey] = it }
            }
        }
    }

    fun reloadWeather(){
        try{
            val newWeather = yumemiWeather.fetchThrowsWeather()
            _weatherInfoData.value = _weatherInfoData.value.copy(weather = newWeather)
        }catch (e: UnknownException){
            showErrorDialog()
        }
    }

    private fun showErrorDialog(){
        _isShowErrorDialog.value = true
    }

    fun closeErrorDialog(){
        _isShowErrorDialog.value = false
    }

}

class FakeWeatherMainViewModel(
    yumemiWeather: YumemiWeather,
    initialWeatherInfoData: WeatherInfoData
) : WeatherMainViewModel(
    initialWeatherInfoData = initialWeatherInfoData,
    yumemiWeather = yumemiWeather,
    savedStateHandle = SavedStateHandle()   //fake(empty)
)
