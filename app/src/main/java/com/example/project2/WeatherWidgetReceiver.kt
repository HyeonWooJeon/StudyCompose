package com.example.project2

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

//appWidgetProvider를 GlanceAppWidgetReceiver에 상속
class WeatherWidgetReceiver : GlanceAppWidgetReceiver()  {
    override val glanceAppWidget: GlanceAppWidget = WeatherWidget()

    // 버튼 클릭 시 이벤트를 처리하는 onReceive
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        CHLog.d("WeatherWidgetReceiver", "onReceive called with action: ${intent.action}")

        // 위젯 업데이트가 발생했을 때, WeatherUpdateActionCallback가 자동으로 처리
        if (intent.action == "com.example.project2.WEATHER_UPDATE") {
            // WeatherUpdateActionCallback에서 처리하므로 추가 로직 필요 없음
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        CHLog.d("weatherUpdate","onUpdate")
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}


