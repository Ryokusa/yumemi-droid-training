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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import jp.co.yumemi.droidtraining.WeatherType

@Composable
fun WeatherApp(
    yumemiWeather:YumemiWeather = YumemiWeather(LocalContext.current),
    initialWeatherInfoData: WeatherInfoData =
        WeatherInfoData(
            weather = yumemiWeather.fetchSimpleWeather(),
            lowestTemperature = 5,
            highestTemperature = 40
        )
){
    var showErrorDialog: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    var weatherInfoData by rememberSaveable {
        mutableStateOf(
            initialWeatherInfoData
        )
    }

    /** 天気を取得。例外があればエラーダイアログ表示 */
    fun reloadWeather(throwUnknownException: (e: UnknownException) -> Unit){
        try{
            val weather = yumemiWeather.fetchThrowsWeather()
            val newWeatherInfoData = weatherInfoData.copy(weather = weather)
            weatherInfoData = newWeatherInfoData
        }catch (e: UnknownException){
            throwUnknownException(e)
        }
    }
    
    WeatherFetchErrorDialog(
        showDialog = showErrorDialog,
        onDismissRequest = { showErrorDialog = false },
        onReload = {
            showErrorDialog = false
            reloadWeather{ showErrorDialog = true }
        }
    )

    WeatherAppContent(
        weatherInfoData = weatherInfoData,
        onReloadClick = {
            reloadWeather{ showErrorDialog = true }
        }
    )
}

@Composable
fun WeatherAppContent(weatherInfoData: WeatherInfoData, onReloadClick: () -> Unit){
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
                    onReloadClick()
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
fun PreviewSunnyWeatherApp(){
    val weatherInfoData = WeatherInfoData(
        weather = WeatherType.SUNNY.weather,
        lowestTemperature = 10,
        highestTemperature = 30
    )
    WeatherApp(initialWeatherInfoData = weatherInfoData)
}

@Composable
@Preview
fun PreviewCloudyWeatherApp(){
    val weatherInfoData = WeatherInfoData(
        weather = WeatherType.CLOUDY.weather,
        lowestTemperature = 10,
        highestTemperature = 30
    )
    WeatherApp(initialWeatherInfoData = weatherInfoData)
}

@Composable
@Preview
fun PreviewRainyWeatherApp(){
    val weatherInfoData = WeatherInfoData(
        weather = WeatherType.RAINY.weather,
        lowestTemperature = 10,
        highestTemperature = 30
    )
    WeatherApp(initialWeatherInfoData = weatherInfoData)
}

@Composable
@Preview
fun PreviewSnowWeatherApp(){
    val weatherInfoData = WeatherInfoData(
        weather = WeatherType.SNOW.weather,
        lowestTemperature = 10,
        highestTemperature = 30
    )
    WeatherApp(initialWeatherInfoData = weatherInfoData)
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
