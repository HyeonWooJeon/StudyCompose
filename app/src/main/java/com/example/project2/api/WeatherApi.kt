package com.example.project2.api

import androidx.camera.core.ImageProcessor.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    //get방식으로 요청
    @GET("/v1/current.json")
    fun getWeather(
        //apikey값 전달
        @Query("key") apikey : String,
        //파라미터에 검색할 도시이름 전달
        @Query("q") city : String
    //WeatherModel 객체를 담은 응답을 반환
    //callback 방식으로 수정 Responce파트
    ) : Call<WeatherModel>
}