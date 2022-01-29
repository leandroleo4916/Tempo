package com.example.tempo.activity.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tempo.activity.search.SearchActivity
import com.example.tempo.utils.ConstantsCidades
import com.example.tempo.utils.SecurityPreferences
import com.example.tempo.databinding.ActivityMainBinding
import com.example.tempo.remote.ApiServiceMain
import com.example.tempo.remote.Periodo
import com.example.tempo.remote.RetrofitClientMain
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val securityPreferences: SecurityPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle = Bundle()
        bundle.getString(ConstantsCidades.CIDADES.ID)

        if (bundle.isEmpty){
            verifyCidades()
            listener()
        }
        else{ observer(bundle.toString()) }
    }

    private fun verifyCidades() {
        val id = ""//securityPreferences.getStoredString(ConstantsCidades.CIDADES.ID)
        if (id.isEmpty()) { observer("2702108") }
        else { observer("2702108") }
    }

    private fun observer(id: String) {

        val remote = RetrofitClientMain().createService(ApiServiceMain::class.java)
        val call: Call<Map<String, Map<String, Periodo>>> = remote.tempo(id)
        call.enqueue(object : Callback<Map<String, Map<String, Periodo>>> {
            override fun onResponse(call: Call<Map<String, Map<String, Periodo>>>,
                                    response: Response<Map<String, Map<String, Periodo>>>) {
                val res = response.code()
                val r = response.body()

            }

            override fun onFailure(call: Call<Map<String, Map<String, Periodo>>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun listener(){
        binding.imageAddLocalization.setOnClickListener { openActivity() }
    }

    private fun openActivity(){
        startActivity(Intent(this, SearchActivity::class.java))
        finish()
    }
}