package jp.co.yumemi.droidtraining

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.api.YumemiWeather
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class WeatherMainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val weatherInfoDataRepository: WeatherInfoDataRepository
): ViewModel() {
    private val isShowErrorDialogKey = "isShowErrorDialog"
    private val _isShowErrorDialog = MutableStateFlow(
        savedStateHandle.get<Boolean>(isShowErrorDialogKey) ?: false
    )
    val isShowErrorDialog = _isShowErrorDialog.asStateFlow()

    private val weatherInfoDataKey = "weatherInfoData"
    val weatherInfoData: StateFlow<WeatherInfoData>

    init {
        savedStateHandle.get<WeatherInfoData>(weatherInfoDataKey)?.let{ it ->
            weatherInfoDataRepository.setWeatherInfoData(it)
        }
        weatherInfoData = weatherInfoDataRepository.weatherInfoData

        viewModelScope.launch {
            launch {
                weatherInfoData.collect{savedStateHandle[weatherInfoDataKey] = it}
            }
            launch {
                isShowErrorDialog.collect { savedStateHandle[isShowErrorDialogKey] = it }
            }
        }
    }

    fun reloadWeather(){
        try{
            weatherInfoDataRepository.updateWeatherInfoData()
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
    weatherInfoDataRepository = WeatherInfoDataRepository(yumemiWeather, initialWeatherInfoData),
    savedStateHandle = SavedStateHandle()   //fake(empty)
)
