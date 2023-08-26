package jp.co.yumemi.droidtraining.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import jp.co.yumemi.droidtraining.R
import jp.co.yumemi.droidtraining.WeatherInfoData


@Composable
fun WeatherApp(){
    //TODO: 実際のデータに入れ替える
    val weather = WeatherInfoData(R.drawable.foo, "foo", 10, 20)

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val screenWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }
        val width = screenWidth / 2
        Column(modifier = Modifier.width(width)) {
            Spacer(modifier = Modifier.weight(1f))
            WeatherInfo(weather)
            ActionButtons(modifier = Modifier
                .padding(top = 80.dp)
                .weight(1f)
            )
        }
    }
}

@Composable
fun WeatherInfo(weather: WeatherInfoData, modifier: Modifier = Modifier){
    val weatherState by remember {
        mutableStateOf(weather)
    }
    Column(modifier = modifier) {
        Image(
            painter = painterResource(id = weather.icon),
            contentDescription = "MainImage",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RectangleShape)
        )
        Row {
            Text(
                text = "${weatherState.lowestTemperature}℃",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
                color = Color.Blue
            )
            Text(
                text = "${weatherState.highestTemperature}℃",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
                color = Color.Red
            )
        }
    }
}

@Composable
fun ActionButtons(
    modifier: Modifier = Modifier,
    onReloadClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button( onClick = onReloadClick) {
            Text(text = stringResource(id = R.string.reload))
        }
        Button(onClick = onNextClick) {
            Text(text = stringResource(id = R.string.next))
        }
    }
}

@Composable
@Preview
fun PreviewWeatherApp(){
    WeatherApp()
}

@Composable
@Preview
fun PreviewWeatherInfo(){
    val weather = WeatherInfoData(R.drawable.foo, "foo", 10, 20)
    WeatherInfo(weather = weather)
}

@Composable
@Preview
fun PreviewActionButtons(){
    ActionButtons()
}
