package com.example.tempo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.tempo.R
import com.example.tempo.dataclass.TimeDataClass

class SevenDaysAdapter: RecyclerView.Adapter<SevenDaysAdapter.SevenDaysViewHolder>() {

    private var data = ArrayList<TimeDataClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SevenDaysViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_tempo, parent, false)

        return SevenDaysViewHolder(item)
    }

    override fun onBindViewHolder(holder: SevenDaysViewHolder, position: Int) {
        val time = data[position]
        holder.bind(time, position)
    }

    override fun getItemCount() = data.size

    inner class SevenDaysViewHolder (itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val position = adapterPosition
            when(view){ } //itemView -> listener.clickRecycler(id, cidade) }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(weather: TimeDataClass, position: Int){
            itemView.run {
                findViewById<TextView>(R.id.text_time).text = weather.time
                val image = findViewById<ImageView>(R.id.image_icon_time)
                image.setImageResource(weather.icon)
                findViewById<TextView>(R.id.text_temperature).text = weather.temperature
                findViewById<TextView>(R.id.text_humidity).text = weather.humidity+"%"
                if (position == 23) {
                    findViewById<Toolbar>(R.id.toolbar_div).visibility = View.GONE
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateWeatherSevenDays(weather: ArrayList<TimeDataClass>) {
        data = weather
        notifyDataSetChanged()
    }
}