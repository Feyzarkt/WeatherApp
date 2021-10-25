package com.feyzaurkut.weather_app.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feyzaurkut.weather_app.R
import com.feyzaurkut.weather_app.adapter.RecyclerViewAdapter
import com.feyzaurkut.weather_app.databinding.ActivityMainBinding
import com.feyzaurkut.weather_app.model.WeatherResponse
import com.feyzaurkut.weather_app.service.ApiClient
import com.feyzaurkut.weather_app.service.ApiService
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {

    private lateinit var apiService: ApiService
    private lateinit var job : Job
    private lateinit var recyclerViewAdapter : RecyclerViewAdapter
    private lateinit var weatherList: ArrayList<WeatherResponse>
    private lateinit var binding: ActivityMainBinding
    private var permissionLauncher: ActivityResultLauncher<String>? = null

    val exceptionHandler = CoroutineExceptionHandler{ coroutineContext, throwable ->
        println("Error: ${throwable.localizedMessage}")
    }

    private lateinit var locationManager: LocationManager
    var lattlong =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setTitle("WEATHER APP")

        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        registerLauncher()
        getLocation()

    }

    private fun getLocation() {
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val manifestPermission = Manifest.permission.ACCESS_FINE_LOCATION
        val permissionStatus = ContextCompat.checkSelfPermission(this, manifestPermission )

        if (permissionStatus == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, manifestPermission)) {
                Snackbar.make(binding.root, "Permission needed for location", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission") {
                    permissionLauncher?.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }.show()
            } else {
                permissionLauncher?.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, locationListener)
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val userLatitude = String.format("%.3f", location.latitude)
            val userLongitude = String.format("%.3f", location.longitude)
            lattlong =  userLatitude +","+ userLongitude

            loadData()
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {
            Toast.makeText(this@MainActivity, "Permisson needed!", Toast.LENGTH_LONG).show()
        }
    }

    private fun registerLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                //permission granted
                if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
                   println("getLocation")
                }
            } else {
                //permission denied
                Toast.makeText(this@MainActivity, "Permisson needed!", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun loadData(){
        apiService =  ApiClient.getClient().create(ApiService::class.java)

        job = CoroutineScope(Dispatchers.IO).launch {

            val response = apiService.listWeather(lattlong)

            withContext(Dispatchers.Main + exceptionHandler){
                if (response.isSuccessful) {
                    response.body()?.let {
                        weatherList = ArrayList(it)

                        weatherList.let {
                            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
                            recyclerView.addItemDecoration(
                                    DividerItemDecoration(
                                            recyclerView.getContext(),
                                            DividerItemDecoration.VERTICAL
                                    )
                            )
                            recyclerView.adapter = recyclerViewAdapter
                        }
                    }
                }
            }

        }
        job.invokeOnCompletion { println("loadData job denied") }
    }

    override fun onItemClick(weatherModel: WeatherResponse) {
        Toast.makeText(this, "Clicked : ${weatherModel.title}", Toast.LENGTH_LONG).show()
    }

}



