package jp.co.yumemi.droidtraining

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import jp.co.yumemi.droidtraining.components.WeatherApp
import jp.co.yumemi.droidtraining.theme.YumemiTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YumemiTheme {
                WeatherApp()
            }
        }
    }
}
