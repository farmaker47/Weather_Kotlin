package com.george.weather_kotlin.di

import com.george.weather_kotlin.network.WeatherApiService
import com.george.weather_kotlin.network.WeatherJsonObject
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val weatherApiService: WeatherApiService
) : WeatherApiService {

    override fun getProperties(
        lat: String?,
        lon: String?,
        units: String,
        appid: String
    ): Response<WeatherJsonObject> = weatherApiService.getProperties(
        lat,
        lon,
        units,
        appid
    )
}