package jp.co.yumemi.droidtraining.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.co.yumemi.droidtraining.WeatherType
import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.viewmodels.FakeForecastWeatherViewModel
import jp.co.yumemi.droidtraining.viewmodels.ForecastWeatherViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun WeatherAppDetailContent(
    weatherInfoData: WeatherInfoData,
    modifier: Modifier = Modifier,
    viewModel: ForecastWeatherViewModel = hiltViewModel(),
) {
    val forecastWeatherInfoDataList by viewModel.forecastWeatherInfoDataList.collectAsStateWithLifecycle()
    val fetching by viewModel.forecastFetching.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        WeatherInfoDataPlaceText(place = weatherInfoData.place)
        ForecastWeatherInfoDataList(forecastWeatherInfoDataList = forecastWeatherInfoDataList)
    }

    DisposableEffect(LocalLifecycleOwner.current) {
        viewModel.fetchForecastWeather {}
        onDispose {
            viewModel.cancelFetchForecastWeather()
        }
    }

    // TODO: エラーダイアログ

    if (fetching) {
        LoadingOverlay()
    }
}

@Composable
fun WeatherInfoDataPlaceText(place: String) {
    Text(
        text = place,
        fontSize = 36.sp,
    )
}

@Composable
fun ForecastWeatherInfoData(forecastWeatherInfoData: WeatherInfoData) {
    val dateText = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        .format(forecastWeatherInfoData.dateTime)
    val timeText = DateTimeFormatter.ofPattern("HH時")
        .format(forecastWeatherInfoData.dateTime)
    val dateTimeText = """$dateText
        |$timeText
    """.trimMargin()
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        ),
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 20.dp)
            .fillMaxWidth()
            .height(70.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = dateTimeText,
                modifier = Modifier
                    .weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            )
            WeatherInfoIcon(
                weatherInfoData = forecastWeatherInfoData,
                modifier = Modifier.fillMaxHeight(),
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "${forecastWeatherInfoData.temperature}℃",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                )
                WeatherTemperatureText(weather = forecastWeatherInfoData)
            }
        }
    }
}

@Composable
fun ForecastWeatherInfoDataList(forecastWeatherInfoDataList: List<WeatherInfoData>) {
    LazyColumn {
        items(forecastWeatherInfoDataList) { forecastWeatherInfoData ->
            ForecastWeatherInfoData(forecastWeatherInfoData)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherAppDetailContentPreview() {
    val initialWeatherInfoData = WeatherInfoData(
        weather = WeatherType.SUNNY,
        lowestTemperature = 10,
        highestTemperature = 20,
        place = "岐阜",
        temperature = 15,
        dateTime = LocalDateTime.now(),
    )

    val fakeForecastWeatherInfoData = WeatherInfoData(
        weather = WeatherType.SUNNY,
        lowestTemperature = 10,
        highestTemperature = 20,
        place = "岐阜",
        temperature = 15,
        dateTime = LocalDateTime.now(),
    )
    val fakeForecastWeatherInfoDataList = mutableListOf<WeatherInfoData>()
    for (i in 1..10) {
        fakeForecastWeatherInfoDataList.add(
            fakeForecastWeatherInfoData.copy(
                lowestTemperature = i.toShort(),
                highestTemperature = (i + 10).toShort(),
            ),
        )
    }

    WeatherAppDetailContent(
        initialWeatherInfoData,
        viewModel = FakeForecastWeatherViewModel(
            initialForecastWeatherInfoDataList = fakeForecastWeatherInfoDataList,
        ),
    )
}

@Preview
@Composable
fun ForecastWeatherInfoPreview(
    @PreviewParameter(WeatherAppPreviewParameterProvider::class)
    weatherInfoData: WeatherInfoData,
) {
    ForecastWeatherInfoData(weatherInfoData)
}
