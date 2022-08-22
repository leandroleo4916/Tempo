package com.example.tempo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tempo.R
import com.example.tempo.dataclass.InfoCidade

class MainAdapter: RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var data = arrayListOf<InfoCidade?>()
    private var listCities = arrayListOf<InfoCidade>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_tempo, parent, false)

        return MainViewHolder(item)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val city = listCities[position]
        holder.run { city }

    }

    override fun getItemCount(): Int {
        return listCities.size
    }

    inner class MainViewHolder (itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val position = adapterPosition
            val id = listCities[position]
            val cidade = listCities[position]

            when(view){ } //itemView -> listener.clickRecycler(id, cidade) }
        }

        fun bind(cidade: String, estado: String){
            itemView.run {
                findViewById<TextView>(R.id.text_time).text = cidade
                findViewById<TextView>(R.id.text_estado).text = estado
            }
        }
    }

    fun updateMain(list: ArrayList<InfoCidade?>){
        data = list
        notifyDataSetChanged()
    }
}