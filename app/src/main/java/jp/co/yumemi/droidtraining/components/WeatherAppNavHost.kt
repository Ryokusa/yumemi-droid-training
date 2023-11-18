package jp.co.yumemi.droidtraining.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jp.co.yumemi.droidtraining.viewmodels.ForecastWeatherViewModel
import jp.co.yumemi.droidtraining.viewmodels.WeatherMainViewModel

@Composable
fun WeatherAppNavHost(
    navController: NavHostController,
    mainViewModel: WeatherMainViewModel,
    forecastWeatherViewModel: ForecastWeatherViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Route.WeatherMain.name,
        modifier = modifier,
    ) {
        composable(Route.WeatherMain.name) {
            WeatherAppMainContent(
                viewModel = mainViewModel,
                onNextClick = {
                    navController.navigate(Route.WeatherDetail.name) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(Route.WeatherDetail.name) {
            WeatherAppDetailContent(
                viewModel = forecastWeatherViewModel,
            )
        }
    }
}
