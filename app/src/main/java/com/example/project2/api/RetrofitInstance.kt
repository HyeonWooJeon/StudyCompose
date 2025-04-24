package com.example.project2.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    //api 기본 url
    private const val baseUrl = "https://api.weatherapi.com";

    private fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //check service 추가해서 getData
    val weatherApi: WeatherApi by lazy {
        //WeatherApi interface내 사용할 URL 추가 주소 및 api키, Query(검색어)
        getInstance().create(WeatherApi::class.java)
    }
}