package com.example.skie

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skie.ui.WeatherViewModel
import com.example.skie.ui.screens.SearchScreen
import com.example.skie.ui.screens.WeatherScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNav(modifier: Modifier = Modifier){
    val navController = rememberNavController()
    val viewModel: WeatherViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "weather",
        modifier = modifier
    ){
        composable("weather"){
            val selectedLocation = it.savedStateHandle
                .getStateFlow<String?>("selected_location", null)
                .collectAsStateWithLifecycle()

            LaunchedEffect(selectedLocation.value) {
                selectedLocation.value?.let{location ->
                    viewModel.fetchWeather(location)
                }
            }

            WeatherScreen(
                onSearchClick = {navController.navigate("search")},
                viewModel = viewModel
            )
        }
        composable(
            "search",
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            }
        ) {
            SearchScreen(
                onLocationSelected = { location ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_location", location)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}