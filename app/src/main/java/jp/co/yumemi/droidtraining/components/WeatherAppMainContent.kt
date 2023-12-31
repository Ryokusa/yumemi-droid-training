package jp.co.yumemi.droidtraining.components

import WeatherFetchErrorDialog
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.co.yumemi.droidtraining.R
import jp.co.yumemi.droidtraining.WeatherType
import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.viewmodels.FakeWeatherMainViewModel
import jp.co.yumemi.droidtraining.viewmodels.WeatherMainViewModel
import java.time.LocalDateTime

@Composable
fun WeatherAppMainContent(
    modifier: Modifier = Modifier,
    viewModel: WeatherMainViewModel = hiltViewModel(),
    onNextClick: () -> Unit,
) {
    val weatherInfoData by viewModel.weatherInfoData.collectAsStateWithLifecycle()
    val updating by viewModel.updating.collectAsStateWithLifecycle()
    val showErrorDialog by viewModel.showErrorDialog.collectAsStateWithLifecycle()

    WeatherFetchErrorDialog(
        showDialog = showErrorDialog,
        onDismissRequest = { viewModel.closeErrorDialog() },
        onReload = {
            viewModel.closeErrorDialog()
            viewModel.reloadWeather()
        },
    )

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
                Text(
                    text = weatherInfoData?.place ?: stringResource(id = R.string.unknown_weather),
                    fontSize = 20.sp,
                )
            }
            weatherInfoData?.let {
                WeatherInfo(it)
            } ?: WeatherUnknownIcon(Modifier.fillMaxWidth())

            ActionButtons(
                modifier = Modifier
                    .padding(top = 80.dp)
                    .weight(1f),
                onReloadClick = {
                    viewModel.reloadWeather()
                },
                onNextClick = {
                    onNextClick()
                },
                reloadEnabled = !updating,
                nextEnabled = !updating && weatherInfoData != null,
            )
        }
    }

    if (updating) {
        LoadingOverlay()
    }
}

@Composable
fun WeatherInfo(weatherInfoData: WeatherInfoData, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        WeatherInfoIcon(
            weatherInfoData = weatherInfoData,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RectangleShape),
        )
        WeatherTemperatureText(weather = weatherInfoData)
    }
}

@Composable
fun WeatherUnknownIcon(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.unknown),
        contentDescription = "unknown",
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}

@Composable
fun WeatherInfoIcon(weatherInfoData: WeatherInfoData, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = weatherInfoData.icon),
        contentDescription = weatherInfoData.weather.id,
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
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
    reloadEnabled: Boolean = true,
    nextEnabled: Boolean = true,
    onReloadClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(onClick = onReloadClick, enabled = reloadEnabled) {
            Text(
                text = stringResource(id = R.string.reload),
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Button(onClick = onNextClick, enabled = nextEnabled) {
            Text(
                text = stringResource(id = R.string.next),
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
@Preview
fun PreviewUnknownAppMainContent() {
    val fakeViewModel = FakeWeatherMainViewModel(
        initialWeatherInfoData = null,
        updatedWeatherInfoData = null,
    )
    WeatherAppMainContent(viewModel = fakeViewModel, onNextClick = {})
}

@Composable
@Preview
fun PreviewWeatherInfo() {
    val weather = WeatherInfoData(WeatherType.SUNNY, 10, 20, "岐阜", 15, LocalDateTime.now())
    WeatherInfo(weatherInfoData = weather)
}

@Composable
@Preview
fun PreviewActionButtons() {
    ActionButtons()
}
