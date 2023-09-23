package jp.co.yumemi.droidtraining

import android.content.Context
import com.example.weatherapi.api.CurrentWeatherDataAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.api.YumemiWeather
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import jp.co.yumemi.droidtraining.usecases.UpdateWeatherInfoDataUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModules {
    @Provides
    @Singleton
    fun provideYumemiWeather(@ApplicationContext context: Context): YumemiWeather {
        return YumemiWeather(context = context)
    }

    @Provides
    @Singleton
    fun provideCurrentWeatherDataAPI(): CurrentWeatherDataAPI {
        return CurrentWeatherDataAPI(BuildConfig.API_KEY)
    }

    @Provides
    @Singleton
    fun provideWeatherInfoDataRepository(
        currentWeatherDataAPI: CurrentWeatherDataAPI,
    ): WeatherInfoDataRepository {
        return WeatherInfoDataRepository(
            currentWeatherDataAPI = currentWeatherDataAPI,
        )
    }

    @Provides
    @Singleton
    fun provideUpdateWeatherInfoDataUseCase(repository: WeatherInfoDataRepository): UpdateWeatherInfoDataUseCase {
        return UpdateWeatherInfoDataUseCase(repository)
    }
}
