package com.example.tempo.activity.main

import WeatherDataClass
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tempo.activity.ShowToast
import com.example.tempo.activity.search.SearchActivity
import com.example.tempo.adapter.MainAdapter
import com.example.tempo.constants.ConstantsCities
import com.example.tempo.databinding.ActivityMainBinding
import com.example.tempo.dataclass.TimeDataClass
import com.example.tempo.repository.ResultRequest
import com.example.tempo.repository.WeatherType
import com.example.tempo.security.SecurityPreferences
import com.example.tempo.utils.CaptureDateCurrent
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val securityPreferences: SecurityPreferences by inject()
    private val weatherViewModel: WeatherViewModel by viewModel()
    private val showToast: ShowToast by inject()
    private val captureDateCurrent = CaptureDateCurrent()
    private lateinit var adapter: MainAdapter
    private lateinit var id: String
    private lateinit var city: String
    private lateinit var uf: String
    private lateinit var latitude: String
    private lateinit var longitude: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        searchCity()

        if (city.isEmpty()) openActivity()
        else {
            recycler()
            observer()
            listener()
        }
    }

    private fun searchCity(){
        id = securityPreferences.getStoredString(ConstantsCities.CITY.ID)
        city = securityPreferences.getStoredString(ConstantsCities.CITY.NOME)
        uf = securityPreferences.getStoredString(ConstantsCities.CITY.UF)
        latitude = securityPreferences.getStoredString(ConstantsCities.CITY.LAT)
        longitude = securityPreferences.getStoredString(ConstantsCities.CITY.LON)
    }

    private fun recycler() {
        val recycler = binding.recyclerViewMain
        adapter = MainAdapter()
        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recycler.adapter = adapter
    }

    private fun observer() {
        weatherViewModel.searchDataWeather(latitude, longitude).observe(this){
            it?.let { result ->
                when (result) {
                    is ResultRequest.Success -> {
                        result.dado?.let { res ->
                            addElementViewTime(res)
                            getData(res)
                        }
                    }
                    is ResultRequest.Error -> {
                        result.exception.message?.let { it1 -> showToast.toastMessage(it1, this) }
                    }
                    is ResultRequest.ErrorConnection -> {
                        result.exception.message?.let { it1 -> showToast.toastMessage(it1, this) }
                    }
                }
            }
        }
    }

    private fun addElementViewTime(res: WeatherDataClass) {

        val dateDay = captureDateCurrent.captureDateDay()
        val hora = captureDateCurrent.captureHoraCurrent()

        binding.run {
            progressMain.visibility = View.GONE
            val fell = res.hourly.apparentTemperature[0]
            var fellUnit = fell.toInt()
            val time = res.currentWeather.temperature
            var timeUnit = time.toInt()
            val max = res.daily.temperature2MMax[0]
            var maxUnit = max.toInt()
            val min = res.daily.temperature2MMin[0]
            var minUnit = min.toInt()
            val code = WeatherType.weatherCode(res.currentWeather.weathercode.toInt())

            if (fell > fellUnit+0.5) fellUnit += 1
            if (time > timeUnit+0.5) timeUnit += 1
            if (max > maxUnit+0.5) maxUnit += 1
            if (min > minUnit+0.5) minUnit += 1

            "$city - $uf".also { textviewCidade.text = it }
            code.weatherDesc.also { textviewCeu.text = it }
            "$dateDay - $hora".also { textviewDate.text = it }
            "Humidade ${res.hourly.relativehumidity2M[0]}%".also { textviewHumidity.text = it }
            ("Sessação térmica de ${fellUnit}º").also { textviewTermica.text = it }
            "$timeUnit".also { textViewTemperatura.text = it }
            ("$maxUnit"+"º/"+"${minUnit}º").also { textviewMaxmin.text = it }

            progressMain.visibility = View.GONE
            textviewCidade.visibility = View.VISIBLE
            textviewDate.visibility = View.VISIBLE
            textViewTemperatura.visibility = View.VISIBLE
            textCelsius.visibility = View.VISIBLE
            textviewCeu.visibility = View.VISIBLE
            textviewMaxmin.visibility = View.VISIBLE
            textviewTermica.visibility = View.VISIBLE
            textviewHumidity.visibility = View.VISIBLE
        }
    }

    private fun listener(){
        binding.imageAddLocalization.setOnClickListener { openActivity() }
    }

    private fun openActivity(){
        startActivity(Intent(this, SearchActivity::class.java))
    }

    override fun onRestart() {
        super.onRestart()
        searchCity()
        observer()
    }

    private fun getData(weather: WeatherDataClass){
        val data = ArrayList<TimeDataClass>()
        for (i in 0..23){
            val time = weather.hourly.time[i].split("T")
            val temp = weather.hourly.temperature2M[i]
            var temperature = temp.toInt()
            if (temp > temperature+0.5) temperature += 1

            data.add(TimeDataClass(time[1], temperature.toString(),
                weather.hourly.relativehumidity2M[i].toString()))
        }
        adapter.updateWeatherPerHour(data)
    }
}