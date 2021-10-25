package com.feyzaurkut.weather_app.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feyzaurkut.weather_app.R
import com.feyzaurkut.weather_app.adapter.RecyclerViewAdapter_Details
import com.feyzaurkut.weather_app.model.Consolidated_weather
import com.feyzaurkut.weather_app.service.ApiClient
import com.feyzaurkut.weather_app.service.ApiServiceDetails
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.coroutines.*


class DetailsActivity : AppCompatActivity() {

    private lateinit var apiService: ApiServiceDetails
    private lateinit var job : Job
    private lateinit var recyclerViewAdapter : RecyclerViewAdapter_Details
    private lateinit var weatherDetailsList: ArrayList<Consolidated_weather>

    val exceptionHandler = CoroutineExceptionHandler{ coroutineContext, throwable ->
        println("Error: ${throwable.localizedMessage}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView2.layoutManager = layoutManager

        val intent = intent
        var title = intent.getSerializableExtra("adapt_title") as String
        var woeid = intent.getSerializableExtra("adapt_woeid") as Int

        supportActionBar?.setTitle(title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadData(String.format("%d", woeid))
    }

    private fun loadData(woeid: String){
        apiService =  ApiClient.getClient().create(ApiServiceDetails::class.java)

        job = CoroutineScope(Dispatchers.IO).launch {

            val response = apiService.listWeatherDetails(woeid)

            withContext(Dispatchers.Main + exceptionHandler){
                if (response.isSuccessful) {
                    response.body()?.let {
                        weatherDetailsList = ArrayList(it.consolidated_weather)

                        weatherDetailsList.let {
                            recyclerViewAdapter = RecyclerViewAdapter_Details(it)
                            recyclerView2.addItemDecoration(
                                DividerItemDecoration(
                                    recyclerView2.getContext(),
                                    DividerItemDecoration.VERTICAL
                                )
                            )
                            recyclerView2.adapter= recyclerViewAdapter
                        }
                    }
                }
            }

        }
        job.invokeOnCompletion { println("loadData job denied") }
    }

}