package jp.co.yumemi.droidtraining

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import jp.co.yumemi.droidtraining.components.WeatherApp
import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.repository.FakeWeatherInfoDataRepository
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import jp.co.yumemi.droidtraining.theme.YumemiTheme
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import java.time.LocalDateTime

@UninstallModules(HiltModules::class)
@HiltAndroidTest
class WeatherAppUiHiltTest {

    private val _initialWeatherInfoData = WeatherInfoData(
        weather = WeatherType.SUNNY,
        lowestTemperature = 10,
        highestTemperature = 20,
        place = "岐阜",
        temperature = 15,
        dateTime = LocalDateTime.now(),
    )
    private val _updatedWeatherInfoData = WeatherInfoData(
        weather = WeatherType.CLOUDY,
        lowestTemperature = 5,
        highestTemperature = 15,
        place = "東京",
        temperature = 10,
        dateTime = LocalDateTime.of(2021, 1, 1, 12, 0),
    )

    @BindValue
    val weatherInfoDataRepository: WeatherInfoDataRepository = FakeWeatherInfoDataRepository(
        initialWeatherInfoData = _initialWeatherInfoData,
        updatedWeatherInfoData = _updatedWeatherInfoData,
    )

    private val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
    private val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val rule: RuleChain = RuleChain.outerRule(hiltRule).around(composeTestRule)

    private fun assertIsDisplayedWeatherInfoData(weatherInfoData: WeatherInfoData) {
        composeTestRule.onNodeWithText(weatherInfoData.place).assertIsDisplayed()
        composeTestRule.onNodeWithText("${weatherInfoData.lowestTemperature}℃")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("${weatherInfoData.highestTemperature}℃")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(weatherInfoData.weather.id)
            .assertIsDisplayed()
    }

    @Test
    fun canShowWeatherInfo() {
        composeTestRule.setContent {
            YumemiTheme {
                WeatherApp()
            }
        }

        assertIsDisplayedWeatherInfoData(_initialWeatherInfoData)
    }
}
