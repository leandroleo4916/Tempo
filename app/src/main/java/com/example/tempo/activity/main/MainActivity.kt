package com.example.tempo.activity.main

import WeatherDataClass
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tempo.activity.ShowToast
import com.example.tempo.activity.search.SearchActivity
import com.example.tempo.adapter.MainAdapter
import com.example.tempo.constants.ConstantsCities
import com.example.tempo.databinding.ActivityMainBinding
import com.example.tempo.repository.ResultRequest
import com.example.tempo.repository.WeatherType
import com.example.tempo.security.SecurityPreferences
import com.example.tempo.utils.CaptureDateCurrent
import com.example.tempo.utils.ConverterPhoto
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val securityPreferences: SecurityPreferences by inject()
    private val weatherViewModel: WeatherViewModel by viewModel()
    private val showToast: ShowToast by inject()
    private val captureDateCurrent = CaptureDateCurrent()
    private val converterPhoto = ConverterPhoto()
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
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun observer() {
        weatherViewModel.searchDataWeather(latitude, longitude).observe(this){
            it?.let { result ->
                when (result) {
                    is ResultRequest.Success -> {
                        result.dado?.let { res ->
                            addElementViewTime(res)
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
            var fell = res.latitude
            var time = res.currentWeather.temperature
            var max = res.daily.temperature2MMax[0]
            var min = res.daily.temperature2MMin[0]
            val code = WeatherType.weatherCode(res.currentWeather.weathercode.toInt())
            if (fell > fell.toInt()+0.5) fell += 1
            if (time > time.toInt()+0.5) time += 1
            if (max > max.toInt()+0.5) max += 1
            if (min > min.toInt()+0.5) min += 1

            textviewCidade.text = "$city"+" - "+"$uf"
            textviewCeu.text = code.weatherDesc
            textviewDate.text = "$dateDay - $hora"
            textviewHumidity.text = "Humidade ${res.hourly.relativehumidity2M[0]}%"
            textviewTermica.text = "Sessação térmica de ${fell.toInt()}"+"º"
            textViewTemperatura.text = "${time.toInt()}"
            textviewMaxmin.text = "${max.toInt()}"+"º/"+"${min.toInt()}"+"º"

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
}