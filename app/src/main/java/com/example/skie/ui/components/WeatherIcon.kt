package com.example.skie.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*

enum class WeatherIconType {
    SUNNY, PARTLY_CLOUDY, CLOUDY, OVERCAST,
    RAINY, HEAVY_RAIN, STORMY, SNOWY,
    FOGGY, CLEAR_NIGHT, CLOUDY_NIGHT
}

fun conditionCodeToIconType(code: Int, isDay: Int): WeatherIconType {
    return when (code) {
        1000 -> if (isDay == 1) WeatherIconType.SUNNY else WeatherIconType.CLEAR_NIGHT
        1003 -> if (isDay == 1) WeatherIconType.PARTLY_CLOUDY else WeatherIconType.CLOUDY_NIGHT
        1006 -> WeatherIconType.CLOUDY
        1009 -> WeatherIconType.OVERCAST
        1030, 1135, 1147 -> WeatherIconType.FOGGY
        1063, 1150, 1153, 1180, 1183, 1186, 1189 -> WeatherIconType.RAINY
        1192, 1195, 1243, 1246 -> WeatherIconType.HEAVY_RAIN
        1087, 1273, 1276, 1279, 1282 -> WeatherIconType.STORMY
        1114, 1117, 1210, 1213, 1216, 1219,
        1222, 1225, 1255, 1258 -> WeatherIconType.SNOWY
        else -> if (isDay == 1) WeatherIconType.SUNNY else WeatherIconType.CLEAR_NIGHT
    }
}

fun iconTypeToLottieAsset(iconType: WeatherIconType): String {
    return when (iconType) {
        WeatherIconType.SUNNY -> "sunny.json"
        WeatherIconType.PARTLY_CLOUDY -> "partly_cloudy.json"
        WeatherIconType.CLOUDY -> "cloudy.json"
        WeatherIconType.OVERCAST -> "cloudy.json"
        WeatherIconType.RAINY -> "rainy.json"
        WeatherIconType.HEAVY_RAIN -> "rainy.json"
        WeatherIconType.STORMY -> "stormy.json"
        WeatherIconType.SNOWY -> "snowy.json"
        WeatherIconType.FOGGY -> "foggy.json"
        WeatherIconType.CLEAR_NIGHT -> "clear_night.json"
        WeatherIconType.CLOUDY_NIGHT -> "cloudy_night.json"
    }
}

//icon handler
@Composable
fun WeatherIcon(
    code: Int,
    isDay: Int,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    isHero: Boolean = false
) {
    val iconType = conditionCodeToIconType(code, isDay)
    val asset = iconTypeToLottieAsset(iconType)

    LottieWeatherIcon(
        asset = asset,
        size = size,
        modifier = modifier,
        isHero = isHero
    )
}

@Composable
fun LottieWeatherIcon(
    asset: String, //json file turned string now
    size: Dp,
    modifier: Modifier = Modifier,
    isHero: Boolean = false  //move if hero, stay still if not
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset(asset)
    )

    if (isHero) {
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true
        )
        LottieAnimation(
            composition = composition,
            progress = { progress }, //loops
            modifier = modifier.size(size)
        )
    } else {
        LottieAnimation(
            composition = composition,
            progress = { 1f }, //frozen
            modifier = modifier.size(size)
        )
    }

//    val progress by animateLottieCompositionAsState(
//        composition = composition,
//        // hero icon loops forever, hourly icons play once and hold
//        iterations = if (isHero) LottieConstants.IterateForever else 1,
//        isPlaying = true
//    )

//    LottieAnimation(
//        composition = composition,
//        progress = { progress },
//        modifier = modifier.then(
//            Modifier.then(
//                androidx.compose.ui.Modifier.size(size)
//            )
//        )
//    )
}


