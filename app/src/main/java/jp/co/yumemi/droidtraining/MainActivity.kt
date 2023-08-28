package jp.co.yumemi.droidtraining

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }
}

@Composable
fun WeatherApp(){
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val screenWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() }
        val width = screenWidth / 2
        ConstraintLayout(modifier = Modifier.width(width).fillMaxHeight()) {
            val (mainContent, buttons) = createRefs()

            MainContent(modifier = Modifier.constrainAs(mainContent){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            })
            HorizontalCenterButtons(modifier = Modifier.constrainAs(buttons){
                top.linkTo(mainContent.bottom)
            }
                .padding(top = 80.dp))
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier){
    Column(modifier = modifier) {
        MainImage()
        BottomText()
    }
}

@Composable
fun MainImage(){
    Image(
        painter = painterResource(id = R.drawable.foo),
        contentDescription = "MainImage",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RectangleShape)
    )
}

@Composable
fun BottomText(){
    Row {
        Text(text = "left", textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
        Text(text = "right", textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
    }
}

@Composable
fun HorizontalCenterButtons(modifier: Modifier = Modifier){
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Button( onClick = { /*TODO*/ }) {
            Text(text = "Btn1")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Btn2")
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
fun PreviewMainImage(){
    MainImage()
}

@Composable
@Preview
fun PreviewHorizontalCenterButtons(){
    HorizontalCenterButtons()
}
