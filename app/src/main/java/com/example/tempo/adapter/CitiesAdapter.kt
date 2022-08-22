package com.example.tempo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tempo.R
import com.example.tempo.dataclass.Cidades
import com.example.tempo.dataclass.CityData
import com.example.tempo.interfaces.OnItemClickRecycler
import java.util.*

class CitiesAdapter(private val listener: OnItemClickRecycler):
    RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder>(), Filterable {

    private var data = mutableListOf<Cidades>()
    private var listCities = mutableListOf<Cidades>()
    private var filter: ListItemFilter = ListItemFilter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_cidades, parent, false)

        return CitiesViewHolder(item)
    }

    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        val employee = listCities[position]
        holder.run { bind(employee.nome, employee.microrregiao.mesorregiao.uf.nome) }
    }

    override fun getItemCount() = listCities.size

    override fun getFilter() = filter

    inner class CitiesViewHolder (itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val position = adapterPosition
            val id = listCities[position].id
            val city = listCities[position].nome
            val state = listCities[position].microrregiao.mesorregiao.uf.sigla

            when(view){ itemView ->
                listener.clickCity(CityData(0, id, city, state, "", "")) }
        }

        fun bind(city: String, state: String){
            itemView.run {
                findViewById<TextView>(R.id.text_time).text = city
                findViewById<TextView>(R.id.text_estado).text = state
            }
        }
    }

    fun updateCities(list: ArrayList<Cidades>){
        data = list
        getFilter()
        notifyDataSetChanged()
    }

    inner class ListItemFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filterResults = FilterResults()

            if (constraint != null && constraint != "") {

                val list = ArrayList<Cidades>()
                for (city in data) {
                    if (city.nome.lowercase(Locale.ROOT)
                            .contains(
                                constraint.toString()
                                    .lowercase(Locale.ROOT)
                            )
                    ) {
                        list.add(city)
                        when (list.size){ 10 -> break }
                    }
                }
                filterResults.count = list.size
                filterResults.values = list
            }
            else {
                listCities.removeAll(listCities)
                filterResults.count = listCities.size
                filterResults.values = listCities
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {

            if (results.values is ArrayList<*>) {
                listCities = results.values as MutableList<Cidades>
                when { listCities.isEmpty() -> {
                    listCities = results.values as MutableList<Cidades>
                } }
            }
            notifyDataSetChanged()
        }
    }
}