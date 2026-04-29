package com.example.skie.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.example.skie.data.model.HourlyData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//the entire row
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyForecastRow(hourlyData: List<HourlyData>){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        hourlyData
            .filter { hour ->
                val hourTime = LocalDateTime.parse(  //string into datetime object
                    hour.time,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                )
                //for ahead hourly differences
                hourTime.isAfter(LocalDateTime.now().minusHours(1))
            }
            //.take(12) //next 12 hours
            .forEach { hour ->
                HourlyItem(hour = hour) //ui item for each reamining hour
            }
    }
}

//each hour
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HourlyItem(hour: HourlyData){
    val timeFormatted = try{
        val parsed = LocalDateTime.parse(
            hour.time,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") //read using this pattern
        )
        when {
            parsed.hour == 0 -> "12am"
            parsed.hour < 12 -> "${parsed.hour}am"
            parsed.hour == 12 -> "12pm"
            else -> "${parsed.hour - 12}pm" //convert 24h raw string from the api to 12h clock time
        }
    } catch (e: Exception){
        hour.time
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        //displays time
        Text(
            text = timeFormatted,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp
        )
        //displays the weather icon based on the code
        WeatherIcon(
            code = hour.conditionCode,
            isDay = hour.iDay,
            size = 28.dp,
            isHero = false
        )
        //display temp, converts it from double to int
        Text(
            text = "${hour.tempC.toInt()}°",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}