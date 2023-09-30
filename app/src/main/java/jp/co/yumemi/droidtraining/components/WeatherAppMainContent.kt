package jp.co.yumemi.droidtraining.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.droidtraining.WeatherType
import jp.co.yumemi.droidtraining.model.WeatherInfoData

@Composable
fun WeatherAppMainContent(
    modifier: Modifier = Modifier,
    weatherInfoData: WeatherInfoData,
    enabled: Boolean = true,
    onReloadClick: () -> Unit,
    onNextClick: () -> Unit,
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
                onNextClick = {
                    onNextClick()
                },
                enabled = enabled,
            )
        }
    }
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
