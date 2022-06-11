package com.example.tempo.activity.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tempo.activity.adapter.CidadesAdapter
import com.example.tempo.databinding.ActivitySearchBinding
import com.example.tempo.interfaces.OnItemClickRecycler
import com.example.tempo.remote.ApiServiceSearch
import com.example.tempo.remote.Cidades
import com.example.tempo.remote.RetrofitClient
import com.example.tempo.repository.RepositoryCities
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.android.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.properties.Delegates

class SearchActivity : AppCompatActivity(), OnItemClickRecycler {

    private val repositoryCities: RepositoryCities by inject()
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private lateinit var adapter: CidadesAdapter
    private lateinit var client: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private val permissionCode = 1000
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var gpsActive by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        recycler()
        observer()
        searchCitiesFilter()
        listener()

    }

    override fun onRestart() {
        super.onRestart()
        gpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (gpsActive) {
            getPosition()
        }
        else toastMessage("Gps não ativado!")
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

    override fun clickCity(id: String, city: String, state: String) {
        repositoryCities.storeCity(id, city, state)
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
            checkGpsActive()
        }
        else {
            permissionGps()
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkGpsActive(){
        gpsActive = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (gpsActive) getPosition()
        else activeGpsDialog()
    }

    @SuppressLint("MissingPermission")
    private fun getPosition(){

        client = LocationServices.getFusedLocationProviderClient(this)
        client.lastLocation
            .addOnSuccessListener {
                if (it != null){
                    latitude = it.latitude
                    longitude = it.longitude
                    getAddress()
                }
                else {
                    toastMessage("Não foi possível obter sua localização!")
                }
            }
            .addOnFailureListener {
                toastMessage("Não foi possível obter sua localização!")
            }
    }

    private fun permissionGps(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), permissionCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            permissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    checkGpsActive()
                } else {
                    toastMessage("Permissão negada!")
                }
            }
        }
    }

    private fun getAddress(){

        val geocode = Geocoder(applicationContext)
        try {
            val addresses: List<Address> = geocode.getFromLocation(latitude, longitude, 1)
            val address = addresses[0].getAddressLine(0)
            val ind = address.split(",")
            val cityAndState = ind[2]
            val cityDiv = cityAndState.split(" - ")
            val city = cityDiv[0].trim()
            val state = cityDiv[1].trim()
            val id = "0"

            repositoryCities.storeCity(id, city, state)
            finish()

        } catch (e: Exception) {
            toastMessage("Não conseguimos obter o endereço!")
        }
    }

    private fun activeGpsDialog() {
        val dialogClickListenerPositive =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
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
            .setNegativeButton("Cancelar", dialogClickListenerNegative)
            .create()
            .show()
    }

    private fun toastMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}