package com.example.project2


import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.layout.*
import androidx.glance.text.Text
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.text.TextStyle


class WeatherWidget : GlanceAppWidget() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val sharedPreferences =
            context.getSharedPreferences("WeatherWidgetPrefs", Context.MODE_PRIVATE)

        val cityName = sharedPreferences.getString("cityName", "Unknown")
        val temperature = sharedPreferences.getString("temperature", "N/A")
        val condition = sharedPreferences.getString("condition", "N/A")

        provideContent {
            GlanceTheme {
                Column(
                    modifier = GlanceModifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
//                    Text(text = "현재 날씨", style = TextStyle(fontSize = 16.sp))
//                    Spacer(modifier = GlanceModifier.height(2.dp))
                    Text(text = "$temperature °C", style = TextStyle(fontSize = 20.sp))
                    Spacer(modifier = GlanceModifier.height(2.dp))
                    Text(text = cityName ?: "Unknown", style = TextStyle(fontSize = 12.sp))
                    Spacer(modifier = GlanceModifier.height(2.dp))
                    Text(text = condition ?: "N/A", style = TextStyle(fontSize = 12.sp))
                    Spacer(modifier = GlanceModifier.height(4.dp))

                    Button(
                        modifier = GlanceModifier
                            .size(width = 90.dp, height = 40.dp)
                            .background(color = R.color.cg_20),
                        text = "Update",
                        onClick = actionRunCallback<WeatherUpdateActionCallback>()
                    )

                }
            }
        }
    }
}
