package com.shihab.weathernow.data.repository

import com.shihab.weathernow.api.WeatherApiService
import com.shihab.weathernow.data.model.WeatherResponse
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: WeatherApiService
) {
    suspend fun getWeather(city: String, apiKey: String): WeatherResponse {
        return apiService.getWeatherByCity(city, apiKey)
    }
}
