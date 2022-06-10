package com.example.tempo.activity.search

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
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
    private lateinit var client: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private val permissionCode = 1000
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

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

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (gpsActive) {
                client.lastLocation
                    .addOnSuccessListener {
                        if (it != null){
                            latitude = it.latitude
                            longitude = it.longitude
                        }
                        else {
                            toastMessage("Não foi possível obter sua localização!")
                        }
                    }
                    .addOnFailureListener {
                        toastMessage("Não foi possível obter sua localização!")
                    }
            }
            else {
                permissionGps()
            }
        }
    }

    private fun permissionGps(){
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
    }

    override fun onRequestPermissionsResult (requestCode: Int,
                                             permissions: Array<out String>,
                                             grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val r = requestCode
        val p = permissions
        val g = grantResults
        createNoGpsDialog()
    }

    private fun createNoGpsDialog() {
        val dialogClickListenerPositive =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val callGPSSettingIntent = Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        )
                        startActivity(callGPSSettingIntent)
                    }
                }
            }
        val dialogClickListenerNegative =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_NEGATIVE -> {
                        toastMessage("GPS não foi ativado!")
                    }
                }
            }
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Por favor, ative seu GPS.")
            .setPositiveButton("Ativar", dialogClickListenerPositive)
            .setNegativeButton("Não Ativar", dialogClickListenerNegative)
            .create()
            .show()

    }

    private fun toastMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}