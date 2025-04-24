package com.example.project2


import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.example.project2.api.Condition
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

//class WeatherUpdateService : Service() {
//    //GPS로 위도, 경도, 지역 구하는 변수
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//
//    //startService로 onStartCommand 시작
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        CHLog.d("WeatherService","서비스 onStartCommand")
//        //별도의 스레드 작업을 처리
//        Thread {
//            //업데이트 위치 확인을 위한 Context
//            CHLog.d("WeatherService","onStartCommand 내부 Thread 시작")
//            val context = this.applicationContext
//
//            //test
//            val glanceId = intent?.getIntExtra("appWidgetId", -1) ?: -1
////            CHLog.d("WeatherService","위젯 아이디 :$appWidgetId")
//            val glanceManager = GlanceAppWidgetManager(applicationContext)
//            CoroutineScope(Dispatchers.IO).launch {
////                val glanceId = glanceManager.getGlanceIdBy(appWidgetId)
//                CHLog.d("WeatherService","glanceId 아이디 :$glanceId")
//                getLocationAndUpdateWeather(applicationContext, glanceId)
//            }
//
//            //위젯 고유 식별자 확인을 위해 intent에서 glanceId 추출
////            val glanceId: GlanceId? = intent?.getParcelableExtra("glanceId", GlanceId::class.java)
////            CHLog.d("WeatherService","onStartCommand 내부 glanceId: $glanceId")
////            glanceId?.let {
////                // context, glanceId 담아서 위치구하기
////                getLocationAndUpdateWeather(context, it)
////            }
//
//            // 서비스 종료
//            stopSelf()
//        }.start()
//        //onStartCommand이후 서비스 종료시키면 pending intent 받기전에 재시작X
//        return START_NOT_STICKY
//    }
//    //권한처리하지않는 어노테이션, GeoCoder에
//    @SuppressLint("MissingPermission")
//    private fun getLocationAndUpdateWeather(context: Context, glanceId: Int) {
//        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//            if (location != null) {
//                val geocoder = Geocoder(context, Locale.getDefault())
//                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
//                //주소에 대한 정보를 가져오는데 리스트형식에 첫번째 값을 사용해 도시 이름 추출
//                val cityName = addresses?.get(0)?.locality ?: "Unknown"
//                CHLog.d("WeatherService","GPS위치: $cityName.")
//
//                // 외부 API에서 날씨 데이터 가져오기
//                val weatherData = fetchWeatherDataApi(cityName)
//
//                // SharedPreferences에 저장
//                saveWeatherDataToSharedPreferences(weatherData as WeatherData)
//
//                // Dispatchers.Main으로 위젯 업데이트
//                CoroutineScope(Dispatchers.Main).launch {
////                    WeatherWidget().update(context, glanceId)
//                }
//            } else {
//                // 위치를 가져오지 못한 경우
//                CHLog.d("WeatherService","GPS위치를 가져오지 못했습니다.")
//            }
//        }
//    }
//    //지역이름을 매개변수로 WeatherApi에 연결되는 함수 구현 그에 따른 결과갑을 반환해서 ShardPreferences에 넘길예정
//    //현재 return값 더미 데이터
//    private fun fetchWeatherDataApi(cityName: String): Any {
//
//        CHLog.d("WeatherService","fetchWeatherDataApi!!")
//        return WeatherData(cityName, "12", "Clear")
//    }
//
//    // SharedPreferences와 edit을 이용해 putString값 넣어준후 apply
//    private fun saveWeatherDataToSharedPreferences(weatherData: WeatherData) {
//        val sharedPreferences = application.getSharedPreferences("WeatherWidgetPrefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("cityName", weatherData.cityName)
//        editor.putString("temperature", weatherData.temperature)
//        editor.putString("condition", "weatherData.condition")
//        editor.apply()
//    }
//}
////WeatherData에 대한 data 클라스
//data class WeatherData(
//    val cityName: String,
//    val temperature: String,
//    val condition: String
//)
//
