package com.feyzaurkut.weather_app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.feyzaurkut.weather_app.R
import com.feyzaurkut.weather_app.model.WeatherResponse
import com.feyzaurkut.weather_app.view.DetailsActivity
import kotlinx.android.synthetic.main.recycler_row.view.*

class RecyclerViewAdapter(private val dataList: ArrayList<WeatherResponse>, private val listener : Listener): RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {

    interface Listener {
        fun onItemClick(weatherModel: WeatherResponse)
    }

    class RowHolder(view: View) : RecyclerView.ViewHolder(view){
            fun bind(weatherModel: WeatherResponse, listener: Listener){
                itemView.setOnClickListener {
                    listener.onItemClick(weatherModel)
                }
                itemView.recycler_textView.text = weatherModel.title
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row, parent, false)
        return RowHolder(view)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
        holder.bind(dataList[position], listener)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("adapt_title", dataList[position].title)
            intent.putExtra("adapt_woeid", dataList[position].woeid)
            holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}