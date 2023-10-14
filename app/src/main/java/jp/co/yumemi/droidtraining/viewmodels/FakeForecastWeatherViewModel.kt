package jp.co.yumemi.droidtraining.viewmodels

import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.repository.FakeWeatherInfoDataRepository
import jp.co.yumemi.droidtraining.usecases.GetForecastWeatherInfoDataListUseCase
import jp.co.yumemi.droidtraining.usecases.UpdateForecastWeatherInfoDataListUseCase

class FakeForecastWeatherViewModel(
    initialForecastWeatherInfoDataList: List<WeatherInfoData> = listOf(),
    updatedForecastWeatherInfoDataList: List<WeatherInfoData> = initialForecastWeatherInfoDataList,
    fakeWeatherInfoDataRepository: FakeWeatherInfoDataRepository = FakeWeatherInfoDataRepository(
        initialForecastWeatherInfoDataList = initialForecastWeatherInfoDataList,
        updatedForecastWeatherInfoDataList = updatedForecastWeatherInfoDataList,
    ),
) : ForecastWeatherViewModel(
    getForecastWeatherInfoDataListUseCase = GetForecastWeatherInfoDataListUseCase(
        fakeWeatherInfoDataRepository,
    ),
    updateForecastWeatherInfoDataListUseCase = UpdateForecastWeatherInfoDataListUseCase(
        fakeWeatherInfoDataRepository,
    ),
)
