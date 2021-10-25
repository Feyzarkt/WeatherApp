package com.feyzaurkut.weather_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.feyzaurkut.weather_app.R
import com.feyzaurkut.weather_app.model.Consolidated_weather
import kotlinx.android.synthetic.main.recycler_row_detail.view.*

class RecyclerViewAdapter_Details(private val data: ArrayList<Consolidated_weather>): RecyclerView.Adapter<RecyclerViewAdapter_Details.RowHolderDetail>() {

    class RowHolderDetail(view: View) : RecyclerView.ViewHolder(view){

        fun bind(weatherModel: Consolidated_weather){
            itemView.date.text = weatherModel.applicable_date
            val URL = "https://www.metaweather.com//static/img/weather/png/64/"
            Glide.with(this.itemView).load(URL+"${weatherModel.weather_state_abbr}.png").into(itemView.icon)
            itemView.temp.text = String.format("%.2f",weatherModel.min_temp)+" / "+String.format("%.2f",weatherModel.max_temp)+" \u2103"
            itemView.name.text = weatherModel.weather_state_name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolderDetail {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row_detail, parent, false)
        return RowHolderDetail(view)
    }

    override fun onBindViewHolder(holder: RowHolderDetail, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}