package jp.co.yumemi.droidtraining

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.api.YumemiWeather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WeatherMainViewModel @Inject constructor(
    private val yumemiWeather: YumemiWeather,
    initialWeatherInfoData: WeatherInfoData
): ViewModel() {

    private val _isShowErrorDialog = MutableStateFlow(false)
    val isShowErrorDialog = _isShowErrorDialog.asStateFlow()

    private val _weatherInfoData = MutableStateFlow(initialWeatherInfoData)
    val weatherInfoData = _weatherInfoData.asStateFlow()

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
