package com.example.tempo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tempo.R
import com.example.tempo.dataclass.SevenDaysDataClass

class SevenDaysAdapter: RecyclerView.Adapter<SevenDaysAdapter.SevenDaysViewHolder>() {

    private var data = ArrayList<SevenDaysDataClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SevenDaysViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_seven_days, parent, false)

        return SevenDaysViewHolder(item)
    }

    override fun onBindViewHolder(holder: SevenDaysViewHolder, position: Int) {
        val time = data[position]
        holder.bind(time)
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
        fun bind(weather: SevenDaysDataClass){
            itemView.run {
                findViewById<TextView>(R.id.text_today_right).text = weather.dayWeek
                findViewById<TextView>(R.id.text_today_humidity).text = weather.humidity
                val iconDay = findViewById<ImageView>(R.id.icon_day_time)
                val iconNight = findViewById<ImageView>(R.id.icon_night_time)
                iconDay.setImageResource(weather.iconDay)
                iconNight.setImageResource(weather.iconNight)
                findViewById<TextView>(R.id.text_max_min).text = weather.maxMin
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateWeatherSevenDays(weather: ArrayList<SevenDaysDataClass>) {
        data = weather
        notifyDataSetChanged()
    }
}