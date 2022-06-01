package com.example.tempo.activity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tempo.R
import com.example.tempo.interfaces.OnItemClickRecycler
import com.example.tempo.remote.Cidades
import java.util.*
import kotlin.collections.ArrayList

class CidadesAdapter(private val listener: OnItemClickRecycler):
    RecyclerView.Adapter<CidadesAdapter.CidadesViewHolder>(), Filterable {

    private var data = mutableListOf<Cidades>()
    private var listCidades = mutableListOf<Cidades>()
    private var filter: ListItemFilter = ListItemFilter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CidadesViewHolder {
        val item = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_cidades, parent, false)

        return CidadesViewHolder(item)
    }

    override fun onBindViewHolder(holder: CidadesViewHolder, position: Int) {

        val employee = listCidades[position]
        holder.run { bind(employee.nome, employee.microrregiao.mesorregiao.uf.nome) }

    }

    override fun getItemCount(): Int {
        return listCidades.size
    }

    override fun getFilter(): Filter {
        return filter
    }

    inner class CidadesViewHolder (itemView: View): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init { itemView.setOnClickListener(this) }

        override fun onClick(view: View?) {
            val position = adapterPosition
            val id = listCidades[position].id
            val cidade = listCidades[position].nome
            val state = listCidades[position].microrregiao.mesorregiao.uf.sigla

            when(view){ itemView -> listener.clickCity(id, cidade, state) }
        }

        fun bind(cidade: String, estado: String){
            itemView.run {
                findViewById<TextView>(R.id.text_cidade).text = cidade
                findViewById<TextView>(R.id.text_estado).text = estado
            }
        }
    }

    fun updateCidades(list: ArrayList<Cidades>){
        data = list
        getFilter()
        notifyDataSetChanged()
    }

    private inner class ListItemFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filterResults = FilterResults()

            if (constraint != null && constraint != "") {

                val list = ArrayList<Cidades>()
                for (cidade in data) {
                    if (cidade.nome.toLowerCase(Locale.ROOT)
                            .contains(constraint.toString()
                            .toLowerCase(Locale.ROOT))
                    ) {
                        list.add(cidade)
                        when (list.size){ 10 -> break }
                    }
                }
                filterResults.count = list.size
                filterResults.values = list
            }
            else {
                listCidades.removeAll(listCidades)
                filterResults.count = listCidades.size
                filterResults.values = listCidades
            }
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {

            if (results.values is ArrayList<*>) {
                listCidades = results.values as MutableList<Cidades>
                when { listCidades.isEmpty() -> {
                    listCidades = results.values as MutableList<Cidades>
                } }
            }
            notifyDataSetChanged()
        }
    }
}