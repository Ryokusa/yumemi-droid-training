package jp.co.yumemi.droidtraining.viewmodels

import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.repository.FakeWeatherInfoDataRepository
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import jp.co.yumemi.droidtraining.usecases.GetForecastWeatherInfoDataListUseCase
import jp.co.yumemi.droidtraining.usecases.GetWeatherInfoDataUseCase
import jp.co.yumemi.droidtraining.usecases.UpdateForecastWeatherInfoDataListUseCase

class FakeForecastWeatherViewModel(
    initialWeatherInfoData: WeatherInfoData?,
    initialForecastWeatherInfoDataList: List<WeatherInfoData> = listOf(),
    updatedForecastWeatherInfoDataList: List<WeatherInfoData> = initialForecastWeatherInfoDataList,
    fakeWeatherInfoDataRepository: WeatherInfoDataRepository = FakeWeatherInfoDataRepository(
        initialWeatherInfoData = initialWeatherInfoData,
        initialForecastWeatherInfoDataList = initialForecastWeatherInfoDataList,
        updatedForecastWeatherInfoDataList = updatedForecastWeatherInfoDataList,
    ),
) : ForecastWeatherViewModel(
    getWeatherInfoDataUseCase = GetWeatherInfoDataUseCase(
        fakeWeatherInfoDataRepository,
    ),
    getForecastWeatherInfoDataListUseCase = GetForecastWeatherInfoDataListUseCase(
        fakeWeatherInfoDataRepository,
    ),
    updateForecastWeatherInfoDataListUseCase = UpdateForecastWeatherInfoDataListUseCase(
        fakeWeatherInfoDataRepository,
    ),
)
