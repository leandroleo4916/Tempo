package com.example.tempo.activity.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tempo.activity.adapter.CidadesAdapter
import com.example.tempo.activity.main.MainActivity
import com.example.tempo.databinding.ActivitySearchBinding
import com.example.tempo.interfaces.OnItemClickRecycler
import com.example.tempo.remote.ApiServiceSearch
import com.example.tempo.remote.Cidades
import com.example.tempo.remote.RetrofitClient
import com.example.tempo.repository.RepositoryCidades
import com.example.tempo.utils.ConstantsCidades
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity(), OnItemClickRecycler {

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

        val remote = RetrofitClient().createService(ApiServiceSearch::class.java)
        val call: Call<ArrayList<Cidades>> = remote.cidades()
        call.enqueue(object : Callback<ArrayList<Cidades>> {
            override fun onResponse(call: Call<ArrayList<Cidades>>,
                                    res: Response<ArrayList<Cidades>>) {
                res.body()?.let { adapter.updateCidades(it) }
            }

            override fun onFailure(call: Call<ArrayList<Cidades>>, t: Throwable) {
                TODO("Not yet implemented")
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