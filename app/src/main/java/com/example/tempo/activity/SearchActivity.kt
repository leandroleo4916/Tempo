package com.example.tempo.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tempo.activity.utils.ConstantsCidades
import com.example.tempo.databinding.ActivitySearchBinding
import com.example.tempo.interfaces.OnItemClickRecycler
import com.example.tempo.repository.RepositoryCidades
import com.example.tempo.repository.Resultado
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), OnItemClickRecycler {

    private val searchViewModel: SearchViewModel by viewModel()
    private val repositoryCidades: RepositoryCidades by inject()
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private lateinit var adapter: CidadesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recycler()
        observer()
        searchCidadesFilter()

    }

    private fun recycler() {
        val recycler = binding.recyclerCidades
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = CidadesAdapter(this)
        recycler.adapter = adapter
    }

    private fun observer() {
        searchViewModel.searchCidades().observe(this, {
            it?.let { resultado ->
                when (resultado) {
                    is Resultado.Sucesso -> {
                        resultado.dado?.let { it ->
                            adapter.updateCidades(it)
                        }
                    }
                    is Resultado.Erro -> {
                        val res = resultado.exception
                    }
                    is Resultado.ErroConnection -> {
                        val res = resultado.exception
                    }
                }
            }
        })
    }

    private fun searchCidadesFilter() {

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun clickRecycler(id: String, cidade: String) {
        repositoryCidades.storeCidade(id, cidade)
        val bundle = Bundle()
        bundle.putString(ConstantsCidades.CIDADES.ID, id)
        bundle.putString(ConstantsCidades.CIDADES.NOME, cidade)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent, bundle)
        finish()
    }

}