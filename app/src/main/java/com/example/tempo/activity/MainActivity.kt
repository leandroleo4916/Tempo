package com.example.tempo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tempo.activity.utils.ConstantsCidades
import com.example.tempo.activity.utils.SecurityPreferences
import com.example.tempo.databinding.ActivityMainBinding
import com.example.tempo.remote.Tempo
import com.example.tempo.repository.ResultadoMain
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val securityPreferences: SecurityPreferences by inject()
    private val mainViewModel: MainViewModel by viewModel()

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

    private fun verifyCidades(){
        val id = securityPreferences.getStoredString(ConstantsCidades.CIDADES.ID)
        if (id.isEmpty()) { openActivity() }
        else { observer(id) }
    }

    private fun observer(id: String) {
        mainViewModel.getTempo(id).observe(this, {
            it.let { result ->
                when (result) {
                    is ResultadoMain.Success -> {
                        result.dado?.let { it ->
                            setValue(it)
                        }
                    }
                    is ResultadoMain.Error -> {

                    }
                    is ResultadoMain.ErrorConnection -> {

                    }
                }
            }
        })
    }

    private fun setValue(tempo: Tempo){
        val t = tempo.tempo
    }

    private fun listener(){
        binding.imageAddLocalization.setOnClickListener { openActivity() }
    }

    private fun openActivity(){
        startActivity(Intent(this, SearchActivity::class.java))
        finish()
    }
}