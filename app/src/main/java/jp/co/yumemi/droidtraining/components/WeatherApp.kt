package jp.co.yumemi.droidtraining.components

import WeatherFetchErrorDialog
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.co.yumemi.api.YumemiWeather
import jp.co.yumemi.droidtraining.FakeWeatherMainViewModel
import jp.co.yumemi.droidtraining.R
import jp.co.yumemi.droidtraining.WeatherMainViewModel
import jp.co.yumemi.droidtraining.WeatherType
import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.theme.YumemiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(
    mainViewModel: WeatherMainViewModel = hiltViewModel(),
) {
    val showErrorDialog by mainViewModel.isShowErrorDialog.collectAsStateWithLifecycle()

    val weatherInfoData by mainViewModel.weatherInfoData.collectAsStateWithLifecycle()

    val updating by mainViewModel.updating.collectAsStateWithLifecycle()

    WeatherFetchErrorDialog(
        showDialog = showErrorDialog,
        onDismissRequest = { mainViewModel.closeErrorDialog() },
        onReload = {
            mainViewModel.closeErrorDialog()
            mainViewModel.reloadWeather()
        },
    )

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
        NavHost(
            navController = navController,
            startDestination = Route.WeatherMain.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.WeatherMain.name) {
                WeatherAppMainContent(
                    weatherInfoData = weatherInfoData,
                    onReloadClick = {
                        mainViewModel.reloadWeather()
                    },
                    onNextClick = {
                        navController.navigate(Route.WeatherDetail.name)
                    },
                    enabled = !updating,
                )
            }
            composable(Route.WeatherDetail.name) {
                WeatherAppDetailContent(weatherInfoData = weatherInfoData)
            }
        }
    }

    if (updating) {
        LoadingOverlay()
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewWeatherApp() {
    val yumemiWeather = YumemiWeather(context = LocalContext.current)
    val viewModel = FakeWeatherMainViewModel(
        initialWeatherInfoData = WeatherInfoData(
            weather = WeatherType.of(yumemiWeather.fetchSimpleWeather()),
            lowestTemperature = 5,
            highestTemperature = 40,
            place = "岐阜",
            temperature = 10,
        ),
    )
    YumemiTheme {
        WeatherApp(mainViewModel = viewModel)
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun DarkPreviewWeatherApp() {
    val yumemiWeather = YumemiWeather(context = LocalContext.current)
    val viewModel = FakeWeatherMainViewModel(
        initialWeatherInfoData = WeatherInfoData(
            weather = WeatherType.of(yumemiWeather.fetchSimpleWeather()),
            lowestTemperature = 5,
            highestTemperature = 40,
            place = "岐阜",
            temperature = 10,
        ),
    )
    YumemiTheme {
        WeatherApp(mainViewModel = viewModel)
    }
}

class WeatherAppPreviewParameterProvider : PreviewParameterProvider<WeatherInfoData> {
    private val initialWeatherInfoData = WeatherInfoData(
        weather = WeatherType.SUNNY,
        lowestTemperature = 10,
        highestTemperature = 30,
        place = "岐阜",
        temperature = 20,
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
    WeatherApp(mainViewModel = mainViewModel)
}

