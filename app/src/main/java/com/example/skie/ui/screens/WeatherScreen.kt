package com.example.skie.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skie.R
import com.example.skie.data.model.WeatherData
import com.example.skie.ui.components.*
import com.example.skie.data.util.getMoodQuote
import com.example.skie.ui.WeatherUiState
import com.example.skie.ui.WeatherViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    onSearchClick: () -> Unit,
    viewModel: WeatherViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pullToRefreshState = rememberPullToRefreshState()
    val context = LocalContext.current

    //result of the permission request
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            // permission just granted so get GPS
            viewModel.fetchFromGps()
        }
        // if denied viewmodel already loaded auto:ip from init
    }


    LaunchedEffect(Unit) {
        val hasFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        //if both permission sare met
        if (!hasFine && !hasCoarse) {
            // first launch involvs ask for permission
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    PullToRefreshBox(
        isRefreshing = uiState is WeatherUiState.Loading,
        onRefresh = { viewModel.refresh() },
        state = pullToRefreshState,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState) {
                is WeatherUiState.Loading -> LoadingScreen()
                is WeatherUiState.Error -> ErrorScreen(
                    message = state.message,
                    onRetry = { viewModel.refresh() }
                )
                is WeatherUiState.Offline -> ErrorScreen(
                    message = stringResource(R.string.no_internet_connection),
                    onRetry = { viewModel.refresh() }
                )
                is WeatherUiState.Success -> {
                    WeatherContent(
                        data = state.data,
                        onSearchClick = onSearchClick
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherContent(
    data: WeatherData,
    onSearchClick: () -> Unit
){
    val gradient = getWeatherGradient(data.conditionCode, data.isDay)
    val quote = getMoodQuote(data.tempC, data.conditionCode, isDay = data.isDay)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.systemBars)//safe area
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            //top bar with location and search icon
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = data.location,
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search, //default is 48dp for hit slops
                        contentDescription = stringResource(R.string.search),
                        tint = Color.White
                    )
                }
            }

            //icon, temp and condition
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                //animated icon
                WeatherIcon(
                    code = data.conditionCode,
                    isDay = data.isDay,
                    size = 150.dp,
                    isHero = true,
                    modifier = Modifier.padding(end = 16.dp)
                )

                Column{
                    //temp
                    Text(
                        text = "${data.tempC.toInt()}°", //cleaner as integer
                        color = Color.White,
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Thin,
                        lineHeight = 72.sp
                    )
                    //condition
                    Text(
                        text = data.condition,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    //highest and lowest for the day
                    Text(
                        text = "H:${data.highTempC.toInt()}°  L:${data.lowTempC.toInt()}°",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 14.sp
                    )
                    //what it actually feels like
                    Text(
                        text = "Feels like ${data.feelsLikeC.toInt()}°",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        //humidity
                        Text(
                            text = "💧 ${data.humidity}%",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 13.sp
                        )
                        //wind
                        Text(
                            text = "💨 ${data.wind.toInt()} km/h",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            //hourly forecast row
            if(data.hourly.isNotEmpty()) { //only show if data exists
                GlassCard {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        HourlyForecastRow(hourlyData = data.hourly)
                    }
                }
            }

            //mood quote
            GlassCard {
                Text(
                    text = quote.quote,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            //daily forecast section
            //inside box to avoid clips on smaller screens
            if(data.forecast.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    DailyForecastSection(forecastList = data.forecast)
                }
            }
        }
    }
}


fun getWeatherGradient(conditionCode: Int, isDay: Int): Brush {
    val colors = when {
        isDay == 0 -> listOf(Color(0xFF0D1B2A), Color(0xFF1B2A4A), Color(0xFF162447)) //any condition at night
        conditionCode == 1000 -> listOf(Color(0xFFFFB347), Color(0xFFFFD700), Color(0xFF87CEEB)) //clear skies during the day
        conditionCode in listOf(1003) -> listOf(Color(0xFF87CEEB), Color(0xFFB0C4DE), Color(0xFF778899)) //partly cloudy
        conditionCode in listOf(1006, 1009) -> listOf(Color(0xFF78909C), Color(0xFF90A4AE), Color(0xFFB0BEC5))  //more cloudy/overcast
        conditionCode in listOf(1030, 1135, 1147) -> listOf(Color(0xFF9E9E9E), Color(0xFFBDBDBD), Color(0xFFE0E0E0)) //mist/fog
        conditionCode in listOf(1063, 1150, 1153, 1180, 1183,
            1186, 1189, 1192, 1195, 1243, 1246) ->
            listOf(Color(0xFF37474F), Color(0xFF546E7A), Color(0xFF607D8B)) //rain
        conditionCode in listOf(1087, 1273, 1276, 1279, 1282) ->
            listOf(Color(0xFF212121), Color(0xFF37474F), Color(0xFF263238)) //storm
        conditionCode in listOf(1114, 1117, 1210, 1213, 1216,
            1219, 1222, 1225, 1255, 1258) ->
            listOf(Color(0xFFB0BEC5), Color(0xFFCFD8DC), Color(0xFFECEFF1))  //snow
        else -> listOf(Color(0xFF87CEEB), Color(0xFF4FC3F7), Color(0xFF0288D1))  //default look of the app
    }
    return Brush.verticalGradient(colors)
}