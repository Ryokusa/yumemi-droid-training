package jp.co.yumemi.droidtraining.components

import WeatherFetchErrorDialog
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    ) {
        WeatherAppContent(
            modifier = Modifier.padding(it),
            weatherInfoData = weatherInfoData,
            onReloadClick = {
                mainViewModel.reloadWeather()
            },
            enabled = !updating,
        )
    }

    if (updating) {
        LoadingOverlay()
    }
}

@Composable
fun WeatherAppContent(
    modifier: Modifier = Modifier,
    weatherInfoData: WeatherInfoData,
    enabled: Boolean = true,
    onReloadClick: () -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val screenWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }
        val width = screenWidth / 2
        Column(modifier = Modifier.width(width)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(8.dp),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(text = weatherInfoData.place, fontSize = 20.sp)
            }
            WeatherInfo(weatherInfoData)

            ActionButtons(
                modifier = Modifier
                    .padding(top = 80.dp)
                    .weight(1f),
                onReloadClick = {
                    onReloadClick()
                },
                enabled = enabled,
            )
        }
    }
}

@Composable
fun WeatherInfo(weatherInfoData: WeatherInfoData, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Image(
            painter = painterResource(id = weatherInfoData.icon),
            contentDescription = weatherInfoData.weather.id,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RectangleShape),
        )
        WeatherTemperatureText(weather = weatherInfoData)
    }
}

@Composable
fun WeatherTemperatureText(weather: WeatherInfoData) {
    Row {
        Text(
            text = "${weather.lowestTemperature}℃",
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            color = Color.Blue,
        )
        Text(
            text = "${weather.highestTemperature}℃",
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            color = Color.Red,
        )
    }
}

@Composable
fun ActionButtons(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onReloadClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(onClick = onReloadClick, enabled = enabled) {
            Text(
                text = stringResource(id = R.string.reload),
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Button(onClick = onNextClick, enabled = enabled) {
            Text(
                text = stringResource(id = R.string.next),
                style = MaterialTheme.typography.labelMedium,
            )
        }
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

@Composable
@Preview
fun PreviewWeatherInfo() {
    val weather = WeatherInfoData(WeatherType.SUNNY, 10, 20, "岐阜")
    WeatherInfo(weatherInfoData = weather)
}

@Composable
@Preview
fun PreviewActionButtons() {
    ActionButtons()
}
