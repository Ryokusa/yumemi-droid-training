package jp.co.yumemi.droidtraining.viewmodels

import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import jp.co.yumemi.droidtraining.usecases.GetWeatherInfoDataUseCase
import jp.co.yumemi.droidtraining.usecases.UpdateWeatherInfoDataUseCase

// 以下はプレビュー用のフェイク（天気情報初期値を設定できる）
class FakeWeatherMainViewModel(
    initialWeatherInfoData: WeatherInfoData?,
    updatedWeatherInfoData: WeatherInfoData? = initialWeatherInfoData,
    fakeWeatherInfoDataRepository: WeatherInfoDataRepository = FakeWeatherInfoDataRepository(
        initialWeatherInfoData,
        updatedWeatherInfoData,
    ),
) : WeatherMainViewModel(
    updateWeatherInfoDataUseCase = UpdateWeatherInfoDataUseCase(
        fakeWeatherInfoDataRepository,
    ),
    getWeatherInfoDataUseCase = GetWeatherInfoDataUseCase(
        fakeWeatherInfoDataRepository,
    ),
)
