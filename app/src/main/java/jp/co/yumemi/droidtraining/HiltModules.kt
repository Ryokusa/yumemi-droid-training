package jp.co.yumemi.droidtraining

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.api.YumemiWeather
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherMainViewModelModule {
    @Provides
    @Singleton
    fun provideYumemiWeather(@ApplicationContext context: Context): YumemiWeather{
        return YumemiWeather(context = context)
    }

    @Provides
    fun provideInitialWeatherInfoData(yumemiWeather: YumemiWeather): WeatherInfoData{
        return WeatherInfoData(
            weather = yumemiWeather.fetchSimpleWeather(),
            lowestTemperature = 5,
            highestTemperature = 40
        )
    }

    @Provides
    @Singleton
    fun provideWeatherInfoDataRepository(yumemiWeather: YumemiWeather, initialWeatherInfoData: WeatherInfoData): WeatherInfoDataRepository{
        return WeatherInfoDataRepository(yumemiWeather, initialWeatherInfoData)
    }
}
