package com.example.skie.data.util

data class MoodQuote(
    val quote: String,
    val author: String = "Skie"
)
fun getMoodQuote(
    tempC: Double,
    conditionCode: Int,
    isDay: Int
): MoodQuote{
    return when {
        // Stormy
        conditionCode in listOf(1087, 1273, 1276, 1279, 1282) ->
            if (isDay == 1) MoodQuote("Even the sky needs to let it all out sometimes.")
            else MoodQuote("The night is putting on quite a show tonight.")

        // Heavy rain
        conditionCode in listOf(1192, 1195, 1243, 1246) ->
            if (isDay == 1) MoodQuote("The rain is just the sky saying it cares.")
            else MoodQuote("Let the rain sing you to sleep tonight.")

        // Light rain / drizzle
        conditionCode in listOf(1063, 1150, 1153, 1168, 1171, 1180, 1183, 1186, 1189) ->
            if (isDay == 1) MoodQuote("A little rain never hurt a good mood.")
            else MoodQuote("Rain at night — the best soundtrack for rest.")

        // Snowy
        conditionCode in listOf(1114, 1117, 1210, 1213, 1216, 1219, 1222, 1225, 1255, 1258) ->
            if (isDay == 1) MoodQuote("The world looks cleaner under a blanket of snow.")
            else MoodQuote("The night is quieter under the snow. Listen.")

        // Foggy / misty
        conditionCode in listOf(1030, 1135, 1147) ->
            if (isDay == 1) MoodQuote("Some days are meant to be mysterious.")
            else MoodQuote("The night is keeping its secrets close tonight.")

        // Freezing
        tempC < 0 ->
            if (isDay == 1) MoodQuote("Bundle up — even the sun is shivering today.")
            else MoodQuote("The coldest nights make the warmest beds.")

        // Cold
        tempC < 10 ->
            if (isDay == 1) MoodQuote("Cold enough to make hot cocoa feel heroic.")
            else MoodQuote("A cold night. Perfect for staying in.")

        // Cloudy / overcast
        conditionCode in listOf(1006, 1009) ->
            if (isDay == 1) MoodQuote("The clouds are just the sky thinking out loud.")
            else MoodQuote("No stars tonight, but they're still up there.")

        // Partly cloudy
        conditionCode == 1003 ->
            if (isDay == 1) MoodQuote("A little shade never dimmed a bright spirit.")
            else MoodQuote("The moon is playing hide and seek tonight.")

        // Clear night specifically
        conditionCode == 1000 && isDay == 0 ->
            when {
                tempC > 25 -> MoodQuote("A warm clear night. Step outside for a moment.")
                tempC > 15 -> MoodQuote("The stars are out. Don't waste this night indoors.")
                else -> MoodQuote("Clear and quiet. The night is all yours.")
            }

        // Warm / pleasant day
        tempC in 20.0..28.0 && isDay == 1 ->
            MoodQuote("The world is in a good mood today. Match it.")

        // Hot day
        tempC > 28 && isDay == 1 ->
            MoodQuote("The sun is not playing games today.")

        // Warm night
        tempC > 25 && isDay == 0 ->
            MoodQuote("A warm night. The city never really sleeps.")

        // Mild night
        tempC in 15.0..25.0 && isDay == 0 ->
            MoodQuote("Tonight feels just right. Breathe it in.")

        // Default day
        isDay == 1 ->
            MoodQuote("Go touch grass. Today is a gift.")

        // Default night
        else ->
            MoodQuote("Rest well. Tomorrow has its own weather.")
    }
}
