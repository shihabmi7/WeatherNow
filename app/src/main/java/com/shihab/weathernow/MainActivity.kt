package com.shihab.weathernow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shihab.weathernow.data.ResultState
import com.shihab.weathernow.data.model.Main
import com.shihab.weathernow.data.model.Weather
import com.shihab.weathernow.data.model.WeatherResponse
import com.shihab.weathernow.ui.WeatherViewModel
import com.shihab.weathernow.ui.theme.WeatherNowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            WeatherNowTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WeatherScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "🌤 WeatherNow",
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    WeatherScreenContent(
        state = viewModel.weatherState,
        onCitySubmit = { city ->
            viewModel.fetchWeather(city, "c680ee434baf3909d282e9f7c9b4ab67")
        }
    )
}

@Composable
fun WeatherScreenContent(
    state: ResultState<WeatherResponse>,
    onCitySubmit: (String) -> Unit
) {
    var city by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        WeatherTopBar()
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Enter City") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { if (city.isNotBlank()) onCitySubmit(city) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (state) {
            is ResultState.Loading -> CircularProgressIndicator()

            is ResultState.Success -> {
                val weather = state.data
                Text("City: ${weather.name}", fontWeight = FontWeight.Bold,
                    )
                Text("Temp: ${weather.main.temp}°C")
                Text("Condition: ${weather.weather.firstOrNull()?.main}")
            }

            is ResultState.Error -> Text("Error: ${state.message}", color = Color.Red)

            ResultState.Idle -> Text("Please enter a city and press the button.")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WeatherScreenPreview() {
    val dummyData = WeatherResponse(
        name = "Dhaka",
        main = Main(
            temp = 32.0,
            feels_like = 35.0,
            temp_min = 30.0,
            temp_max = 34.0,
            pressure = 1010,
            humidity = 60
        ),
        weather = listOf(
            Weather(
                main = "Clear",
                description = "clear sky",
                icon = "01d"
            )
        )
    )

    WeatherScreenContent(
        state = ResultState.Success(dummyData),
        onCitySubmit = {}
    )
}