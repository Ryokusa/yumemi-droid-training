package jp.co.yumemi.droidtraining.components

import WeatherFetchErrorDialog
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.api.YumemiWeather
import jp.co.yumemi.droidtraining.R
import jp.co.yumemi.droidtraining.WeatherInfoData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp(){
    val yumemiWeather = YumemiWeather(context = LocalContext.current)
    var showErrorDialog: Boolean by remember {
        mutableStateOf(false)
    }

    var weatherInfoData by remember {
        mutableStateOf(
            WeatherInfoData(
                weather = yumemiWeather.fetchSimpleWeather(),
                lowestTemperature = 5,
                highestTemperature = 40
            )
        )
    }

    /** 天気を取得。例外があればエラーダイアログ表示 */
    fun getWeather(){
        try{
            val weather = yumemiWeather.fetchThrowsWeather()
            val newWeatherInfoData = weatherInfoData.copy(weather = weather)
            weatherInfoData = newWeatherInfoData
        }catch (e: UnknownException){
            showErrorDialog = true
        }
    }
    
    WeatherFetchErrorDialog(
        showDialog = showErrorDialog,
        onDismissRequest = { showErrorDialog = false },
        onReload = {
            showErrorDialog = false
            getWeather()
        }
    )

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val screenWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }
        val width = screenWidth / 2
        Column(modifier = Modifier.width(width)) {
            Spacer(modifier = Modifier.weight(1f))
            WeatherInfo(weatherInfoData)

            ActionButtons(modifier = Modifier
                .padding(top = 80.dp)
                .weight(1f),
                onReloadClick = {
                    getWeather()
                }
            )
        }
    }
}

@Composable
fun WeatherInfo(weather: WeatherInfoData, modifier: Modifier = Modifier){

    Column(modifier = modifier) {
        Image(
            painter = painterResource(id = weather.icon),
            contentDescription = "MainImage",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RectangleShape)
        )
        WeatherTemperatureText(weather = weather)
    }
}

@Composable
fun WeatherTemperatureText(weather: WeatherInfoData){
    Row {
        Text(
            text = "${weather.lowestTemperature}℃",
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            color = Color.Blue
        )
        Text(
            text = "${weather.highestTemperature}℃",
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            color = Color.Red
        )
    }
}

@Composable
fun ActionButtons(
    modifier: Modifier = Modifier,
    onReloadClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = onReloadClick) {
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
    val weather = WeatherInfoData( "sunny", 10, 20)
    WeatherInfo(weather = weather)
}

@Composable
@Preview
fun PreviewActionButtons(){
    ActionButtons()
}
