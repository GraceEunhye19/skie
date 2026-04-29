package com.example.skie.ui.components

//called for each upcoming day

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skie.data.model.DailyData
import com.example.skie.ui.components.WeatherIcon
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

//whole section showing a list
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyForecastSection(forecastList: List<DailyData>){
    var visible by remember { mutableStateOf(false) }

    //for enterance animation
    LaunchedEffect(forecastList) {
        visible = false
        visible = true
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        forecastList.forEachIndexed { index, day ->
            AnimatedDailyCard(
                day = day,
                index = index, //so they come in an order instead of at once
                visible = visible,
            )
        }
    }
}

//animated card
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnimatedDailyCard(
    day: DailyData,
    index: Int,
    visible: Boolean
){
    //slide and stop right based on the screen size
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(
                durationMillis = 400, //speed of slide
                delayMillis = index * 120,  // 120ms gap between each
                easing = FastOutSlowInEasing //smoothen move
            )
        ) {
            with(density) {80.dp.roundToPx()}}+ fadeIn(
            animationSpec = tween(
                durationMillis = 400,
                delayMillis = index * 120,
            )
        )
    ){
        DailyForecastCard(day = day)
    }

}

//each animated card
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyForecastCard(day: DailyData){

    //converts raw date to day name like monday
    val dayName = try {
        val date = LocalDate.parse(day.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    } catch (e: Exception) { day.date } //show raw date

    //to show data
    val date = try{
        val date = LocalDate.parse(day.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        "${date.monthValue}-${date.dayOfMonth}" //apr-29
    } catch (e: Exception){""}

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.15f))
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        //Day name
        Column(modifier = Modifier.width(100.dp)) {
            Text(
                text = dayName,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(100.dp)
            )
            Text(
                text = date,
                color = Color.White,
                fontSize = 14.sp
            )
        }

        //chance of rain
        if (day.chanceOfRain > 0) {
            Text(
                text = "💧 ${day.chanceOfRain}%",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        } else {
            //if no chance of rain be a gap
            Spacer(modifier = Modifier.width(48.dp))
        }

        //weather icon
        WeatherIcon(
            code = day.conditionCode,
            isDay = 1, //for the future it will always be the day icons
            size = 32.dp,
            isHero = false //dont animate this nigga
        )

        //high and low temps of the day
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "${day.highTempC.toInt()}°",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${day.lowTempC.toInt()}°",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}