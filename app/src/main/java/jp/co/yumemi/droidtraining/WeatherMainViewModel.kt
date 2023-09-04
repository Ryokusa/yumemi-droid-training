package jp.co.yumemi.droidtraining

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.api.YumemiWeather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WeatherMainViewModel(private val yumemiWeather: YumemiWeather, initialWeatherInfoData: WeatherInfoData): ViewModel() {

    private val _weatherInfoData = MutableStateFlow(initialWeatherInfoData)
    val weatherInfoData = _weatherInfoData.asStateFlow()

    private val _isShowErrorDialogLiveData = MutableLiveData(false)
    val isShowErrorDialogLiveData: LiveData<Boolean>
        get() = _isShowErrorDialogLiveData


    fun reloadWeather(){
        try{
            val newWeather = yumemiWeather.fetchThrowsWeather()
            _weatherInfoData.value = _weatherInfoData.value.copy(weather = newWeather)
        }catch (e: UnknownException){
            showErrorDialog()
        }
    }

    private fun showErrorDialog(){
        _isShowErrorDialogLiveData.value = true
    }

    fun closeErrorDialog(){
        _isShowErrorDialogLiveData.value = false
    }

}
