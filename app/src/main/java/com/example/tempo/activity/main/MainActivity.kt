package com.example.tempo.activity.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tempo.activity.search.SearchActivity
import com.example.tempo.adapter.MainAdapter
import com.example.tempo.databinding.ActivityMainBinding
import com.example.tempo.remote.*
import com.example.tempo.repository.ResultRequest
import com.example.tempo.utils.CaptureDateCurrent
import com.example.tempo.utils.ConstantsCidades
import com.example.tempo.utils.ConverterPhoto
import com.example.tempo.utils.SecurityPreferences
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val securityPreferences: SecurityPreferences by inject()
    private val weatherData: WeatherViewModel by viewModel()
    private val captureDateCurrent = CaptureDateCurrent()
    private val converterPhoto = ConverterPhoto()
    private lateinit var adapter: MainAdapter
    private val list = ArrayList<InfoCidade?>()
    private val dateString = captureDateCurrent.captureDateCurrent()
    private lateinit var id: String
    private lateinit var city: String
    private lateinit var uf: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        searchCity()

        if (city.isEmpty()){
            openActivity()
        }
        else{
            recycler()
            observer()
            listener()
        }
    }

    private fun searchCity(){
        id = securityPreferences.getStoredString(ConstantsCidades.CIDADES.ID)
        city = securityPreferences.getStoredString(ConstantsCidades.CIDADES.NOME)
        uf = securityPreferences.getStoredString(ConstantsCidades.CIDADES.UF)
    }

    private fun observer() {
        weatherData.searchDataWeather(city).observe(this){
            it?.let { result ->
                when (result) {
                    is ResultRequest.Success -> {
                        result.dado?.let { res ->
                            addElementViewTime(res)
                        }
                    }
                    is ResultRequest.Error -> {
                        result.exception.message?.let { it1 -> toastMessage(it1) }
                    }
                    is ResultRequest.ErrorConnection -> {
                        result.exception.message?.let { it1 -> toastMessage(it1) }
                    }
                }
            }
        }
    }

    private fun recycler() {
        val recycler = binding.recyclerViewMain
        adapter = MainAdapter()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun observerInfo(id: String) {

        val remote = RetrofitClientMain().createService(ApiServiceMain::class.java)
        val call: Call<Map<String, Map<String, Periodo>>> = remote.tempo(id)
        call.enqueue(object : Callback<Map<String, Map<String, Periodo>>> {
            override fun onResponse(
                call: Call<Map<String, Map<String, Periodo>>>,
                response: Response<Map<String, Map<String, Periodo>>>
            ) {
                //val manha = response.body()?.get(id)?.get(dateString)?.manha
                //addElementView(manha)
                //list.add(response.body()?.get(id)?.keys)
                //observerInfoAdapter(id)
            }

            override fun onFailure(call: Call<Map<String, Map<String, Periodo>>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun observerInfoAdapter(id: String) {

        val remote = RetrofitClientMain().createService(ApiServiceRecycler::class.java)
        val call: Call<Map<String, Map<String, InfoCidade>>> = remote.tempoRecycler(id)
        call.enqueue(object : Callback<Map<String, Map<String, InfoCidade>>> {
            override fun onResponse(
                call: Call<Map<String, Map<String, InfoCidade>>>,
                response: Response<Map<String, Map<String, InfoCidade>>>
            ) {
                list.add(response.body()?.get(id)?.get(dateString))
                adapter.updateMain(arrayListOf(response.body()?.get(id)?.get(dateString)))
            }

            override fun onFailure(call: Call<Map<String, Map<String, InfoCidade>>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addElementView(res: InfoCidade?) {

        val dateDay = captureDateCurrent.captureDateDay()
        val hora = captureDateCurrent.captureHoraCurrent()

        binding.run {
            progressMain.visibility = View.GONE
            textviewCidade.text = res?.entidade+" - "+res?.uf
            val tempMax = res?.tempMax
            val tempMin = res?.tempMin
            textviewCeu.text = res?.resumo
            textviewDate.text = "$dateDay - $hora"
            textviewMaxmin.text = "$tempMax"+"º /"+"$tempMin"+"º"
        }
    }

    private fun addElementViewTime(res: WeatherDataClass) {

        val dateDay = captureDateCurrent.captureDateDay()
        val hora = captureDateCurrent.captureHoraCurrent()

        binding.run {
            val time = res.main.temp.toInt()
            progressMain.visibility = View.GONE
            val max = res.main.tempMax.toInt()
            var min = res.main.tempMin
            if (min > min.toInt()+0.5) min += 1

            textviewCidade.text = "$city"+" - "+"$uf"
            textviewCeu.text = res.weather[0].description
            textviewDate.text = "$dateDay - $hora"
            textviewHumidity.text = "Humidade ${res.main.humidity}%"
            textviewTermica.text = "Sessação térmica de ${res.main.feelsLike.toInt()}"+"º"
            textViewTemperatura.text = time.toString()
            textviewMaxmin.text = "${res.main.tempMax.toInt()}"+"º /"+"${res.main.tempMax.toInt()}"+"º"
            textviewMaxmin.text = "$max"+"º/"+"${min.toInt()}"+"º"

            progressMain.visibility = View.GONE
            textviewCidade.visibility = View.VISIBLE
            textviewDate.visibility = View.VISIBLE
            textViewTemperatura.visibility = View.VISIBLE
            textCelsius.visibility = View.VISIBLE
            textviewCeu.visibility = View.VISIBLE
            textviewMaxmin.visibility = View.VISIBLE
            textviewTermica.visibility = View.VISIBLE
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

    private fun toastMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}