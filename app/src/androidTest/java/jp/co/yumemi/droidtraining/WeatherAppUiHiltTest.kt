package jp.co.yumemi.droidtraining

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.core.app.ApplicationProvider
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

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private fun setWeatherApp() {
        composeTestRule.setContent {
            YumemiTheme {
                WeatherApp()
            }
        }
    }

    private fun assertIsDisplayedWeatherInfoData(weatherInfoData: WeatherInfoData) {
        composeTestRule.onNodeWithText(weatherInfoData.place).assertIsDisplayed()
        composeTestRule.onNodeWithText("${weatherInfoData.lowestTemperature}℃")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("${weatherInfoData.highestTemperature}℃")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(weatherInfoData.weather.id)
            .assertIsDisplayed()
    }

    private fun clickUpdate() {
        val reloadText = context.getString(R.string.reload)
        composeTestRule.onNodeWithText(reloadText).performClick()
    }

    private fun clickNext() {
        val nextText = context.getString(R.string.next)
        composeTestRule.onNodeWithText(nextText).performClick()
    }

    @Test
    fun canShowWeatherInfo() {
        setWeatherApp()

        assertIsDisplayedWeatherInfoData(_initialWeatherInfoData)
    }

    @Test
    fun canUpdateWeatherInfo() {
        setWeatherApp()

        assertIsDisplayedWeatherInfoData(_initialWeatherInfoData)

        composeTestRule.onRoot().printToLog("before update")

        clickUpdate()

        composeTestRule.onRoot().printToLog("after update")

        assertIsDisplayedWeatherInfoData(_updatedWeatherInfoData)
    }

    @Test
    fun canShowWeatherAppDetailContent() {
        setWeatherApp()
        clickNext()

        composeTestRule.onNodeWithText(_initialWeatherInfoData.place).assertIsDisplayed()
    }
}
