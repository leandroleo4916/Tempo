package com.example.tempo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tempo.R
import com.example.tempo.dataclass.TimeDataClass

class MainAdapter: RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var data = ArrayList<TimeDataClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_tempo, parent, false)

        return MainViewHolder(item)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val time = data[position]
        holder.bind(time)
    }

    override fun getItemCount() = data.size

    inner class MainViewHolder (itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val position = adapterPosition
            when(view){ } //itemView -> listener.clickRecycler(id, cidade) }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(weather: TimeDataClass){
            itemView.run {
                findViewById<TextView>(R.id.text_time).text = weather.time
                val image = findViewById<ImageView>(R.id.image_icon_time)
                image.setImageResource(weather.icon)
                findViewById<TextView>(R.id.text_temperature).text = weather.temperature
                findViewById<TextView>(R.id.text_humidity).text = weather.humidity+"%"
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateWeatherPerHour(weather: ArrayList<TimeDataClass>) {
        data = weather
        notifyDataSetChanged()
    }
}