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
import com.example.tempo.adapter.AdapterHistory
import com.example.tempo.adapter.CitiesAdapter
import com.example.tempo.databinding.ActivitySearchBinding
import com.example.tempo.dataclass.CityData
import com.example.tempo.interfaces.OnItemClickDeleteCity
import com.example.tempo.interfaces.OnItemClickItemCity
import com.example.tempo.interfaces.OnItemClickRecycler
import com.example.tempo.remote.ApiServiceSearch
import com.example.tempo.remote.Cidades
import com.example.tempo.remote.RetrofitClient
import com.example.tempo.repository.RepositoryCities
import com.example.tempo.repository.RepositoryHistory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.properties.Delegates

class SearchActivity : AppCompatActivity(), OnItemClickRecycler, OnItemClickItemCity,
    OnItemClickDeleteCity {

    private val repositoryCities: RepositoryCities by inject()
    private val repositoryHistory: RepositoryHistory by inject()
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private lateinit var adapterCities: CitiesAdapter
    private lateinit var adapterHistory: AdapterHistory
    private val viewModelSearch: SearchViewModel by viewModel()
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

        recyclerCities()
        recyclerHistory()
        observerCities()
        observerHistory()
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

    private fun recyclerCities() {
        val recycler = binding.recyclerCidades
        recycler.layoutManager = LinearLayoutManager(this)
        adapterCities = CitiesAdapter(this)
        recycler.adapter = adapterCities
    }

    private fun recyclerHistory() {
        val recycler = binding.recyclerHistory
        recycler.layoutManager = LinearLayoutManager(this)
        adapterHistory = AdapterHistory(this, this)
        recycler.adapter = adapterHistory
    }

    private fun observerCities() {

        val remote = RetrofitClient().createService(ApiServiceSearch::class.java)
        val call: Call<ArrayList<Cidades>> = remote.cities()
        call.enqueue(object : Callback<ArrayList<Cidades>> {
            override fun onResponse(
                call: Call<ArrayList<Cidades>>,
                res: Response<ArrayList<Cidades>>
            ) {
                res.body()?.let { adapterCities.updateCidades(it) }
            }
            override fun onFailure(call: Call<ArrayList<Cidades>>, t: Throwable) {}
        })

    }

    private fun observerHistory(){
        viewModelSearch.getHistory()
        viewModelSearch.listHistory.observe(this){
            if (it.size != 0 && it != null){
                binding.textViewHistoryHere.visibility = View.INVISIBLE
                adapterHistory.updateHistory(it)
            }
        }
    }

    private fun searchCitiesFilter() {

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapterCities.filter.filter(s.toString())
                binding.recyclerCidades.visibility = View.VISIBLE
                binding.bottomLocation.visibility = View.INVISIBLE
                binding.recyclerHistory.visibility = View.INVISIBLE
                binding.textViewHistoryHere.visibility = View.INVISIBLE
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun clickCity(id: String, city: String, state: String) {
        viewModelSearch.saveHistory(CityData(0, id, city, state))
        repositoryCities.storeCity(id, city, state)
        finish()
    }

    private fun listener(){
        binding.bottomLocation.setOnClickListener { getPermission() }
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

            saveCityAndFinishActivity("0", city, state)

        } catch (e: Exception) {
            toastMessage("Não conseguimos obter o endereço!")
        }
    }

    private fun saveCityAndFinishActivity(id: String, city: String, state: String){
        repositoryCities.storeCity(id, city, state)
        repositoryHistory.saveCity(CityData(0, id, city, state))
        finish()
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

    override fun clickItemCity(item: CityData) {
        saveCityAndFinishActivity(item.idcity, item.city, item.state)
    }

    override fun clickDeleteCity(item: CityData, position: Int) {
        viewModelSearch.deleteHistory(item.id)
        adapterHistory.updateRemoveItem(position)
    }

    private fun toastMessage(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}