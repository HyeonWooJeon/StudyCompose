package com.example.project2;

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.glance.GlanceId
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.project2.api.Constance
import com.example.project2.api.NetworkResponse
import com.example.project2.api.RetrofitInstance
import com.example.project2.api.WeatherModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.lang.Exception
import java.util.Locale


class  WeatherViewModel(application: Application) : AndroidViewModel(application) {

    //geocoder 객체 초기화
    private val geocoder = Geocoder(application, Locale.ENGLISH)

    //api 연결
    private val weatherApi = RetrofitInstance.weatherApi

    //Api로 불러온 결과저장
    val weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()

    //Activity로 날릴 값
    val weatherViewResult: LiveData<NetworkResponse<WeatherModel>> = weatherResult

    // 데이터베이스 검색어 목록 저장
    private val _searchWords = MutableLiveData<List<SearchWord>>()
    val searchWords: LiveData<List<SearchWord>> = _searchWords

    //레포지토리 인스턴스를 초기화
    private val weatherWordRepository = WeatherWordRepository(application)


    // FusedLocationClient 사용 GPS 위도 경도 구하기
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    // WeaterAPI에 넣을 데이터 값
    private val _locationData = MutableLiveData<android.location.Location>()
    val locationData : MutableLiveData<android.location.Location> get() = _locationData

    //도시 이름 변수 (클라 노출용)
    private val _cityName = MutableLiveData<String>()
    val cityName: LiveData<String> get() = _cityName

    //위치 권한 체크 메소드
    fun checkLocationPermission(activity: WeatherActivity): Boolean{
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        )
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    //권한이 없을시 요청
    fun requestLocationPermission(activity: WeatherActivity, requestCode: Int){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            requestCode
        )
    }



// Geocoder을 이용해 위도, 경도, 주소이름을 비동기적으로 가져옴
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getCityNamesLocation(latitude: Double, longitude: Double, callback: (String?) -> Unit) {
        try {
            geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: List<Address>) {
                    if (addresses.isNotEmpty()) {
                        // 도시 이름(locality) 또는 행정 구역 이름(adminArea)을 반환
                        val cityName = addresses[0].locality ?: addresses[0].adminArea
                        CHLog.d("cityNameLog","$cityName")
                        callback(cityName)
                    } else {
                        callback(null)
                    }
                }

                override fun onError(errorMessage: String?) {
                    // Geocoder에서 오류 발생 시 처리
                    callback(null)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null)
        }
    }


    // 도시 이름을 요청하고 API에서 날씨 데이터를 가져오는 메소드
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestWeatherDataLocation(latitude: Double, longitude: Double) {

        val cityName = getCityNamesLocation(latitude, longitude){ cityName ->
            if (cityName != null) {
                // 도시 이름이 null이 아닐 경우 getData 호출
                _cityName.postValue(cityName) // UI 스레드에서 업데이트

                getData(cityName) // getData 호출
//                CHLog.d("cityName", "$cityName")

            } else {
                weatherResult.postValue(NetworkResponse.Error("위치 정보에서 도시 이름을 가져올 수 없습니다."))
            }
        }
    }

    //마지막 찍힌 위치를 가져오기
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: android.location.Location? ->
            location?.let {
                _locationData.value = it
                requestWeatherDataLocation(it.latitude, it.longitude)

            } ?: run {
                weatherResult.value = NetworkResponse.Error("GPS 위치를 가져올 수 없습니다.")
            }
        }.addOnFailureListener {
            weatherResult.value = NetworkResponse.Error("GPS 위치 가져오기에 실패했습니다: ${it.message}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getWidgetData(context: Context, glanceId: GlanceId) {
        getLastLocation()
        CHLog.d("weatherResult", "$cityName")

        weatherResult.observeForever { result ->
            if (result is NetworkResponse.Success) {
                val weatherModel = result.data
                saveWeatherDataToSharedPreferences(
                    weatherModel.location.name,
                    weatherModel.current.temp_c,
                    weatherModel.current.condition.text
                )
                CHLog.d("weatherWidget", "데이터가 저장되었습니다.")

                // 데이터가 성공적으로 저장된 후에 위젯 업데이트
                viewModelScope.launch {
                    WeatherWidget().update(context, glanceId)
                }
            } else if (result is NetworkResponse.Error) {
                CHLog.d("weatherWidget", "데이터 저장 실패.")
            }
        }
    }


    private fun saveWeatherDataToSharedPreferences(cityName: String, temperature: String, condition: String) {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("WeatherWidgetPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // 필요한 데이터를 저장
        editor.putString("cityName", cityName)
        editor.putString("temperature", temperature)
        editor.putString("condition", condition)
        CHLog.d("SharedPreferences putString", "SharedPreferences에 데이터 저장 - $cityName, $temperature, $condition")
        // 데이터를 비동기적으로 저장
        editor.apply()
    }

    //getData 함수 coutinescope대신 enqueue를 이용해서 관리
    fun getData(city: String) {
        CHLog.d("weatherRRR","getData 호출됨 : $city")
        //postValue로 변경하니까 기가막히게 동작
//        weatherResult.value = NetworkResponse.Loading
        weatherResult.postValue(NetworkResponse.Loading)

        val call = weatherApi.getWeather(Constance.apiKey, city)
        call.enqueue(object : Callback<WeatherModel> {
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        weatherResult.value = NetworkResponse.Success(it)
                        CHLog.d("weatherRRR","성공 ${weatherResult.value}")
                    }
                } else {
                    weatherResult.value = NetworkResponse.Error("지역을 다시 확인해주세요")
                    CHLog.d("weatherRRR","응답실패")
                }
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                weatherResult.value = NetworkResponse.Error("오류 발생: ${t.message}")
                CHLog.d("weatherRRR","네트워크 오류발생 : ${t.message}")
            }
        })
    }

//        viewModelScope.launch{
//            try {
//                //getWeater을 구성했던 함수에 apikey값과 도시이름을 담아 respose에 넘겨줌
//                val response = weatherApi.getWeather(Constance.apiKey,city)
//
//                if(response.isSuccessful){
////                CHLog.i("Response = ", response.body().toString())
//                    response.body()?.let{
//                        weatherResult.value = NetworkResponse.Success(it)
//                    }
//                } else {
////                CHLog.i("E Message = ", response.message())
//                    weatherResult.value = NetworkResponse.Error("지역을 다시 확인해주세요")
//                }
//
//            } catch (e : Exception){
//                    weatherResult.value = NetworkResponse.Error("오류 발생!!!")
//            }
//        }


    //검색어 추가
    fun addSearchWord(searchWord: String) {
        CHLog.d("loglog", searchWord)
        val searchWordEntity = SearchWord(searchWord = searchWord)
        viewModelScope.launch {
            weatherWordRepository.addWeatherWord(searchWordEntity) {
                // 추가 후 필요한 작업을 여기에 추가할 수 있음
            }
        }
    }
    // 검색어 초기화
    init {
        getAllSearchWord()
    }

    //  검색어 불러오기
    private fun getAllSearchWord() {
//        viewModelScope.launch {
        weatherWordRepository.getSearchWord().observeForever { searchWordsList ->
            _searchWords.postValue(searchWordsList) // LiveData 값 업데이트
//            }
        }
    }

    // 검색어 삭제
    fun deleteSearchWord(searchWord: SearchWord) {
//        viewModelScope.launch {
        CHLog.d("getLog", "log")
        weatherWordRepository.deleteSearchWord(searchWord) {
            // 삭제 후 필요한 작업을 여기에 추가할 수 있음
            getAllSearchWord()
//            }
        }
    }



}

// 최근검색어 ROOM으로 내부 db에 저장
