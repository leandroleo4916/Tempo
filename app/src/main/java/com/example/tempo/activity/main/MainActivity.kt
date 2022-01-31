package com.example.tempo.activity.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tempo.activity.search.SearchActivity
import com.example.tempo.databinding.ActivityMainBinding
import com.example.tempo.remote.ApiServiceMain
import com.example.tempo.remote.Periodo
import com.example.tempo.remote.RetrofitClientMain
import com.example.tempo.utils.CaptureDateCurrent
import com.example.tempo.utils.ConstantsCidades
import com.example.tempo.utils.ConverterPhoto
import com.example.tempo.utils.SecurityPreferences
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val securityPreferences: SecurityPreferences by inject()
    private val captureDateCurrent = CaptureDateCurrent()
    private val converterPhoto = ConverterPhoto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        hideView()
        val id = securityPreferences.getStoredString(ConstantsCidades.CIDADES.ID)

        if (id.isEmpty()){ openActivity() }
        else{
            observer(id)
            listener()
        }
    }

    private fun hideView(){
        binding.run {
            progressMain.visibility = View.VISIBLE
            textviewCidade.visibility = View.INVISIBLE
            textviewDate.visibility = View.INVISIBLE
            textViewTemperatura.visibility = View.INVISIBLE
            textCelsius.visibility = View.INVISIBLE
            textviewCeu.visibility = View.INVISIBLE
            textviewMaxmin.visibility = View.INVISIBLE
            textviewTermica.visibility = View.INVISIBLE
            imageTempo.visibility = View.INVISIBLE
        }
    }

    private fun verifyCidades() {
        val id = securityPreferences.getStoredString(ConstantsCidades.CIDADES.ID)
        if (id.isNotEmpty()) { observer(id) }
    }

    private fun observer(id: String) {

        val remote = RetrofitClientMain().createService(ApiServiceMain::class.java)
        val call: Call<Map<String, Map<String, Periodo>>> = remote.tempo(id)
        call.enqueue(object : Callback<Map<String, Map<String, Periodo>>> {
            override fun onResponse(call: Call<Map<String, Map<String, Periodo>>>,
                                    response: Response<Map<String, Map<String, Periodo>>>) {
                addElementView(response.body(), id)
            }
            override fun onFailure(call: Call<Map<String, Map<String, Periodo>>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addElementView(body: Map<String, Map<String, Periodo>>?, id: String) {

        val dateString = captureDateCurrent.captureDateCurrent()
        val dateDay = captureDateCurrent.captureDateDay()
        val hora = captureDateCurrent.captureHoraCurrent()
        val map = body?.get(id)
        val today = map?.get(dateString)
        val manha = today?.manha
        val tarde = today?.tarde
        val noite = today?.noite

        binding.run {
            textviewCidade.text = manha?.entidade+" - "+manha?.uf
            textviewDate.text = dateDay+" - "+hora
            textViewTemperatura.text = manha?.tempMax.toString()
            textviewCeu.text = manha?.tempMaxTende
            textviewMaxmin.text = manha?.tempMax.toString()+" / "+manha?.tempMin.toString()
            textviewTermica.text = manha?.estacao
            imageTempo.setImageBitmap(manha?.icone?.let {
                converterPhoto.converterStringByBitmap(it) })

            progressMain.visibility = View.GONE
            textviewCidade.visibility = View.VISIBLE
            textviewDate.visibility = View.VISIBLE
            textViewTemperatura.visibility = View.VISIBLE
            textCelsius.visibility = View.VISIBLE
            textviewCeu.visibility = View.VISIBLE
            textviewMaxmin.visibility = View.VISIBLE
            textviewTermica.visibility = View.VISIBLE
            imageTempo.visibility = View.VISIBLE
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
        hideView()
        verifyCidades()
    }
}