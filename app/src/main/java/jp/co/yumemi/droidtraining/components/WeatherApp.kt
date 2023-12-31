package jp.co.yumemi.droidtraining.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import jp.co.yumemi.api.YumemiWeather
import jp.co.yumemi.droidtraining.R
import jp.co.yumemi.droidtraining.WeatherType
import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.theme.YumemiTheme
import jp.co.yumemi.droidtraining.viewmodels.FakeForecastWeatherViewModel
import jp.co.yumemi.droidtraining.viewmodels.FakeWeatherMainViewModel
import jp.co.yumemi.droidtraining.viewmodels.ForecastWeatherViewModel
import jp.co.yumemi.droidtraining.viewmodels.WeatherMainViewModel
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(
    mainViewModel: WeatherMainViewModel = hiltViewModel(),
    forecastWeatherViewModel: ForecastWeatherViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { innerPadding ->
        WeatherAppNavHost(
            navController = navController,
            mainViewModel = mainViewModel,
            forecastWeatherViewModel = forecastWeatherViewModel,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewWeatherApp() {
    val yumemiWeather = YumemiWeather(context = LocalContext.current)
    val initialWeatherInfoData = WeatherInfoData(
        weather = WeatherType.of(yumemiWeather.fetchSimpleWeather()),
        lowestTemperature = 5,
        highestTemperature = 40,
        place = "岐阜",
        temperature = 10,
        dateTime = LocalDateTime.now(),
    )
    val viewModel = FakeWeatherMainViewModel(
        initialWeatherInfoData = initialWeatherInfoData,
    )
    val forecastWeatherViewModel = FakeForecastWeatherViewModel(
        initialWeatherInfoData = initialWeatherInfoData,
    )
    YumemiTheme {
        WeatherApp(mainViewModel = viewModel, forecastWeatherViewModel = forecastWeatherViewModel)
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun DarkPreviewWeatherApp() {
    val yumemiWeather = YumemiWeather(context = LocalContext.current)
    val initialWeatherInfoData = WeatherInfoData(
        weather = WeatherType.of(yumemiWeather.fetchSimpleWeather()),
        lowestTemperature = 5,
        highestTemperature = 40,
        place = "岐阜",
        temperature = 10,
        dateTime = LocalDateTime.now(),
    )
    val mainViewModel = FakeWeatherMainViewModel(
        initialWeatherInfoData = initialWeatherInfoData,
    )
    val forecastViewModel = FakeForecastWeatherViewModel(
        initialWeatherInfoData = initialWeatherInfoData,
    )
    YumemiTheme {
        WeatherApp(mainViewModel = mainViewModel, forecastWeatherViewModel = forecastViewModel)
    }
}

class WeatherAppPreviewParameterProvider : PreviewParameterProvider<WeatherInfoData> {
    private val initialWeatherInfoData = WeatherInfoData(
        weather = WeatherType.SUNNY,
        lowestTemperature = 10,
        highestTemperature = 30,
        place = "岐阜",
        temperature = 20,
        dateTime = LocalDateTime.now(),
    )

    override val values: Sequence<WeatherInfoData>

    init {
        val allWeatherInfoDataList = WeatherType.values().map { weatherType ->
            initialWeatherInfoData.copy(weather = weatherType)
        }
        this.values = allWeatherInfoDataList.asSequence()
    }
}

@Preview
@Composable
fun PreviewAllWeatherApp(
    @PreviewParameter(WeatherAppPreviewParameterProvider::class)
    weatherInfoData: WeatherInfoData,
) {
    val mainViewModel = FakeWeatherMainViewModel(
        initialWeatherInfoData = weatherInfoData,
    )
    val forecastWeatherViewModel = FakeForecastWeatherViewModel(
        initialWeatherInfoData = weatherInfoData,
    )
    WeatherApp(mainViewModel = mainViewModel, forecastWeatherViewModel = forecastWeatherViewModel)
}

@Preview
@Composable
fun PreviewUnknownWeatherApp() {
    val mainViewModel = FakeWeatherMainViewModel(
        initialWeatherInfoData = null,
    )
    val forecastWeatherViewModel = FakeForecastWeatherViewModel(
        initialWeatherInfoData = null,
    )
    WeatherApp(mainViewModel = mainViewModel, forecastWeatherViewModel = forecastWeatherViewModel)
}
