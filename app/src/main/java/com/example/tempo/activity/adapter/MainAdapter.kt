package com.example.tempo.activity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tempo.R
import com.example.tempo.interfaces.OnItemClickRecycler
import com.example.tempo.remote.InfoCidade

class MainAdapter(private val listener: OnItemClickRecycler):
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var data = arrayListOf<InfoCidade?>()
    private var listCidades = arrayListOf<InfoCidade>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_tempo, parent, false)

        return MainViewHolder(item)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val cidade = listCidades[position]
        holder.run { cidade }

    }

    override fun getItemCount(): Int {
        return listCidades.size
    }

    inner class MainViewHolder (itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val position = adapterPosition
            val id = listCidades[position]
            val cidade = listCidades[position]

            when(view){ } //itemView -> listener.clickRecycler(id, cidade) }
        }

        fun bind(cidade: String, estado: String){
            itemView.run {
                findViewById<TextView>(R.id.text_cidade).text = cidade
                findViewById<TextView>(R.id.text_estado).text = estado
            }
        }
    }

    fun updateMain(list: ArrayList<InfoCidade?>){
        data = list
        notifyDataSetChanged()
    }
}