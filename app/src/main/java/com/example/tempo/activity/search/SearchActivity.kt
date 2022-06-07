package com.example.tempo.activity.search

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tempo.activity.adapter.CidadesAdapter
import com.example.tempo.databinding.ActivitySearchBinding
import com.example.tempo.interfaces.OnItemClickRecycler
import com.example.tempo.remote.ApiServiceSearch
import com.example.tempo.remote.Cidades
import com.example.tempo.remote.RetrofitClient
import com.example.tempo.repository.RepositoryCidades
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity(), OnItemClickRecycler {

    private val repositoryCities: RepositoryCidades by inject()
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private lateinit var adapter: CidadesAdapter
    private lateinit var locationManager: LocationManager
    private lateinit var location: Location
    private val permissionCode = 1000
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var client: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        client = LocationServices.getFusedLocationProviderClient(this)

        recycler()
        observer()
        searchCitiesFilter()
        listener()

    }

    private fun recycler() {
        val recycler = binding.recyclerCidades
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = CidadesAdapter(this)
        recycler.adapter = adapter
    }

    private fun observer() {

        val remote = RetrofitClient().createService(ApiServiceSearch::class.java)
        val call: Call<ArrayList<Cidades>> = remote.cities()
        call.enqueue(object : Callback<ArrayList<Cidades>> {
            override fun onResponse(
                call: Call<ArrayList<Cidades>>,
                res: Response<ArrayList<Cidades>>
            ) {
                res.body()?.let { adapter.updateCidades(it) }
            }

            override fun onFailure(call: Call<ArrayList<Cidades>>, t: Throwable) {}
        })

    }

    private fun searchCitiesFilter() {

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s.toString())
                binding.buttomLocalizacao.visibility = View.INVISIBLE
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun clickCity(id: String, cidade: String, state: String) {
        repositoryCities.storeCidade(id, cidade, state)
        finish()
    }

    private fun listener(){
        binding.buttomLocalizacao.setOnClickListener { getPermission() }
        binding.backActivity.setOnClickListener { finish() }
    }

    private fun getPermission(){

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (gpsActive) {
                return
            }
            else {
                toastMessage("Por favor, ative o GPS")
            }
        }

        client.lastLocation
            .addOnSuccessListener {
                if (it != null){
                    latitude = location.latitude
                    longitude = location.longitude
                }
                else {
                    toastMessage("Não foi possível obter sua localização!")
                }
            }
            .addOnFailureListener {
                toastMessage("Por favor, ative o GPS")
            }
    }

    private fun toastMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}