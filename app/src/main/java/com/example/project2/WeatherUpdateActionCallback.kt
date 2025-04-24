package com.example.project2

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.example.project2.api.Constance
import com.example.project2.api.RetrofitInstance.weatherApi
import com.example.project2.api.WeatherModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.util.Locale

class WeatherUpdateActionCallback : ActionCallback {
    //ACtionCallBack에서 상속받아 onAction으로 특정 이벤트 발생시 처리할 내용들 구현
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
//        val widgetId = glanceId.hashCode()  // 또는 위젯 ID를 추출하는 방법 찾기
//
//        val intent = Intent(context, WeatherUpdateService::class.java).apply {
//            putExtra("widgetId", widgetId)  // 위젯 ID 전달
//        }
//        CHLog.d("onAction","$widgetId")
//        context.startService(intent)
//
//        CHLog.d("onAction", "StartAction")
//        CHLog.d("onAction", "$glanceId")
//        updateWidgetImmediately(context, glanceId)
        // 서비스나 백그라운드 작업을 호출하여 데이터 가져오는 로직 처리
//        val intent = Intent(context, WeatherUpdateService::class.java)
//        context.startService(intent)

//        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(glanceId)
//        if(glanceId != null) {
//            val intent = Intent(context, WeatherUpdateService::class.java).apply {
//                putExtra("appWidgetId",appWidgetId)
//            }
//            context.startService(intent)
//        } else {
//            CHLog.d("onAction", "glanceId is null")
//        }
        getLocationAndUpdateWeather(context, glanceId)

        CHLog.d("onAction", "StartAction")
        CHLog.d("onAction", "$glanceId")
        // UI를 임시로 업데이트 (예: '업데이트 중' 메시지 표시)
//        updateWidgetImmediately(context, glanceId)
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    private suspend fun getLocationAndUpdateWeather(context: Context, glanceId: GlanceId) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
            location.let {
                val geocoder = Geocoder(context, Locale.ENGLISH)
                CHLog.d("WeatherAction", "location: $location.")
                val addresses = location?.let { it1 -> geocoder.getFromLocation(it1.latitude, location.longitude, 1) }
                CHLog.d("WeatherAction", "addresses: $addresses.")
                //주소에 대한 정보를 가져오는데 리스트형식에 첫번째 값을 사용해 도시 이름 추출
                val cityName = addresses?.get(0)?.locality ?: addresses?.get(0)?.adminArea ?: "Unknown"
                CHLog.d("WeatherAction", "City Name: $cityName")
                // 외부 API에서 날씨 데이터 가져오기
                val weatherData = fetchWeatherDataApi(cityName) { weatherData ->
                    if (weatherData != null) {
                        // SharedPreferences에 저장
                        saveWeatherDataToSharedPreferences(context, weatherData)

                        // Dispatchers.Main으로 위젯 업데이트
                        CoroutineScope(Dispatchers.Main).launch {
                            updateWidgetImmediately(context, glanceId)
                        }
                    } else {
                        CHLog.d("WeatherAction", "날씨 데이터 가져오기에 실패했습니다.")
                    }
                }
            }
        }.addOnFailureListener {
            CHLog.d("WeatherAction", "GPS위치 가져오기 실패")
        }

    }
    private fun fetchWeatherDataApi(cityName: String, callback: (WeatherData?) -> Unit) {
        val call = weatherApi.getWeather(Constance.apiKey, cityName)

        call.enqueue(object : retrofit2.Callback<WeatherModel> {  // retrofit2.Callback으로 명확히 지정
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                if (response.isSuccessful) {
                    val weatherModel = response.body() // WeatherModel 데이터 받기
                        CHLog.d("ActionCallback","$weatherModel")
                    CHLog.d("ActionCallback","${weatherModel?.current?.condition?.text}")
                    weatherModel?.let {
                        // 성공적으로 데이터를 받았을 때 WeatherData로 변환
                        val weatherData = WeatherData(
                            cityName = cityName,  // WeatherModel에서 cityName 추출
                            temperature = it.current.temp_c,
//                            it.temp?erature.toString(),  // temperature를 String으로 변환
                            condition = it.current.condition.text
//                            it.condition  // condition을 가져옴

                        )
                        callback(weatherData)  // 성공한 경우 콜백 호출
                    } ?: run {
                        callback(null)  // response.body()가 null일 때
                    }
                } else {
                    callback(null)  // 응답이 성공적이지 않을 때
                }
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                callback(null)  // 네트워크 오류 발생 시 콜백 호출
            }
        })
    }

//
//    private fun fetchWeatherDataApi(cityName: String): Any {
//
//        val weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
//        val call = weatherApi.getWeather(Constance.apiKey,cityName)
//        call.enqueue(object : Callback<WeatherModel>{
//            override fun onResponse(callback: Call<WeatherModel>, response: Response<WeatherModel>) {
//                if (response.isSuccessful) {
//                    response.body()?.let {
//                        weatherResult.value =  NetworkResponse.Success(it)
//                        CHLog.d("ApiAction","성공 ${weatherResult.value}")
//                    }
//            } else {
//                    weatherResult.value = NetworkResponse.Error("지역을 다시 확인해주세요")
//                    CHLog.d("ApiAction","응답실패")
//                }
//                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
//                    weatherResult.value = NetworkResponse.Error("오류 발생: ${t.message}")
//                    CHLog.d("ApiAction","네트워크 오류발생 : ${t.message}")
//                }
//        })
//
////        CHLog.d("WeatherService", "fetchWeatherDataApi!!")
////        return WeatherData(cityName, "122", "Clear!")
//    }

    // SharedPreferences와 edit을 이용해 putString값 넣어준후 apply
    private fun saveWeatherDataToSharedPreferences(context: Context, weatherData: WeatherData) {
        val sharedPreferences =
            context.getSharedPreferences("WeatherWidgetPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("cityName", weatherData.cityName)
        editor.putString("temperature", weatherData.temperature)
        editor.putString("condition", weatherData.condition)
        editor.apply()
    }

    private suspend fun updateWidgetImmediately(context: Context, glanceId: GlanceId) {
        // GlanceAppWidget에서 임시 UI 상태로 업데이트
        WeatherWidget().update(context, glanceId)
    }
}
data class WeatherData(
    val cityName: String,
    val temperature: String,
    val condition: String
)

    // 서비스나 백그라운드 작업을 호출하여 데이터 가져오는 로직 처리
//    if(glanceId != null) {
//        val intent = Intent(context, WeatherUpdateService::class.java).apply {
//            putExtra("glanceId", glanceId.toString())
//        }
//        context.startService(intent)
//    } else {
//        CHLog.d("onAction", "glanceId is null")
//    }



