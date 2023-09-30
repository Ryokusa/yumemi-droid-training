package jp.co.yumemi.droidtraining

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.test.core.app.ApplicationProvider
import jp.co.yumemi.api.UnknownException
import jp.co.yumemi.droidtraining.components.WeatherApp
import jp.co.yumemi.droidtraining.model.WeatherInfoData
import jp.co.yumemi.droidtraining.repository.WeatherInfoDataRepository
import jp.co.yumemi.droidtraining.theme.YumemiTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.junit.Rule
import org.junit.Test

class WeatherAppUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private val _initialWeatherInfoData = WeatherInfoData(
        weather = WeatherType.SUNNY,
        lowestTemperature = 10,
        highestTemperature = 20,
        place = "岐阜",
    )
    private val _updatedWeatherInfoData = WeatherInfoData(
        weather = WeatherType.CLOUDY,
        lowestTemperature = 5,
        highestTemperature = 15,
        place = "東京",
    )

    class FakeWeatherInfoDataRepository(
        private val initialWeatherInfoData: WeatherInfoData,
        private val updatedWeatherInfoData: WeatherInfoData = initialWeatherInfoData,
        private var updateFailCount: Int = 0,
    ) :
        WeatherInfoDataRepository {
        private val _weatherInfoData = MutableStateFlow(initialWeatherInfoData)
        override val weatherInfoData: StateFlow<WeatherInfoData>
            get() = _weatherInfoData.asStateFlow()

        override suspend fun updateWeatherInfoData() {
            if (updateFailCount > 0) {
                updateFailCount -= 1
                throw UnknownException()
            }
            _weatherInfoData.value = updatedWeatherInfoData
        }

        override fun setWeatherInfoData(weatherInfoData: WeatherInfoData) {
            // do nothing
        }
    }

    @Composable
    private fun FakeWeatherApp(
        initialWeatherInfoData: WeatherInfoData = _initialWeatherInfoData,
        updatedWeatherInfoData: WeatherInfoData = initialWeatherInfoData,
        updateFailCount: Int = 0,
    ) {
        YumemiTheme {
            WeatherApp(
                mainViewModel = FakeWeatherMainViewModel(
                    initialWeatherInfoData = initialWeatherInfoData,
                    fakeWeatherInfoDataRepository = FakeWeatherInfoDataRepository(
                        initialWeatherInfoData = initialWeatherInfoData,
                        updatedWeatherInfoData = updatedWeatherInfoData,
                        updateFailCount = updateFailCount,
                    ),
                ),
            )
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

    @Test
    fun isShowWeatherInfo() {
        composeTestRule.setContent { FakeWeatherApp() }

        assertIsDisplayedWeatherInfoData(_initialWeatherInfoData)
    }

    @Test
    fun canUpdateWeatherInfo() {
        val updatedWeatherInfoData = WeatherInfoData(
            weather = WeatherType.CLOUDY,
            lowestTemperature = 5,
            highestTemperature = 15,
            place = "東京",
        )

        composeTestRule.setContent {
            FakeWeatherApp(
                updatedWeatherInfoData = updatedWeatherInfoData,
            )
        }

        assertIsDisplayedWeatherInfoData(_initialWeatherInfoData)

        composeTestRule.onRoot().printToLog("before update")

        clickUpdate()

        composeTestRule.onRoot().printToLog("after update")

        assertIsDisplayedWeatherInfoData(updatedWeatherInfoData)
    }

    @Test
    fun updateWeatherInfo_failed_and_showDialog() {
        composeTestRule.setContent {
            FakeWeatherApp(
                // ダイアログ上の更新ボタン検証用
                updatedWeatherInfoData = _updatedWeatherInfoData,
                updateFailCount = 1,
            )
        }

        clickUpdate()

        composeTestRule.onNodeWithText("Error").assertIsDisplayed()
    }

    @Test
    fun canCloseDialog_after_updateFailed_and_showDialog() {
        updateWeatherInfo_failed_and_showDialog()

        composeTestRule.onNodeWithText("CLOSE").performClick()

        composeTestRule.onNodeWithText("Error").assertDoesNotExist()

        assertIsDisplayedWeatherInfoData(_initialWeatherInfoData)
    }

    @Test
    fun canReload_after_updateFailed_and_showDialog() {
        updateWeatherInfo_failed_and_showDialog()

        composeTestRule.onNodeWithText("RELOAD").performClick()

        composeTestRule.onNodeWithText("Error").assertDoesNotExist()

        assertIsDisplayedWeatherInfoData(_updatedWeatherInfoData)
    }
}
