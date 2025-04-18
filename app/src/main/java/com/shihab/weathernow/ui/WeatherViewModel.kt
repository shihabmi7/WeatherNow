package com.shihab.weathernow.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shihab.weathernow.data.ResultState
import com.shihab.weathernow.data.model.WeatherResponse
import com.shihab.weathernow.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    var weatherState by mutableStateOf<ResultState<WeatherResponse>>(ResultState.Idle)
        private set


    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            weatherState = ResultState.Loading
            try {
                val result = repository.getWeather(city, apiKey)
                weatherState = ResultState.Success(result)
            } catch (e: Exception) {
                weatherState = ResultState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}
