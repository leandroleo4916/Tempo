package com.example.tempo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tempo.R
import com.example.tempo.dataclass.CityData
import com.example.tempo.interfaces.OnClickItemHistoryCity
import com.example.tempo.interfaces.OnItemClickDeleteCity

class AdapterHistory (private val clickItem: OnClickItemHistoryCity,
                      private val clickDelete: OnItemClickDeleteCity):
    RecyclerView.Adapter<AdapterHistory.ViewHolderHistory>() {

    private var listHistory: ArrayList<CityData> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHistory {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_history, parent, false)

        return ViewHolderHistory(item)
    }

    override fun onBindViewHolder(holder: ViewHolderHistory, position: Int) {

        val fullHistory = listHistory[position]
        holder.bindHistory(fullHistory)
    }

    inner class ViewHolderHistory(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val delete: ImageView = itemView.findViewById(R.id.imageDelete)
        private val itemCity: ConstraintLayout = itemView.findViewById(R.id.recycler_cities)
        private val city: TextView = itemView.findViewById(R.id.textView_city)

        init {
            delete.setOnClickListener(this)
            itemCity.setOnClickListener(this)
        }

        fun bindHistory(history: CityData){
            city.text = history.city+" - "+history.state
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            when (view){
                itemCity -> clickItem.clickItemHistoryCity(listHistory[position])
                delete -> clickDelete.clickDeleteCity(listHistory[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return listHistory.count()
    }

    fun updateHistory(list: ArrayList<CityData>) {
        listHistory = if (list.size <= 1){ list }
        else { list.reversed() as ArrayList<CityData> }
        notifyDataSetChanged()
    }

    fun updateRemoveItem(position: Int){
        listHistory.removeAt(position)
        notifyItemRemoved(position)
        notificationRemove()
    }

    fun updateRemoveAll(list: ArrayList<CityData>) {
        listHistory.removeAll(list.toSet())
        if (list.size >= 2){
            notifyItemRangeRemoved(0, list.size)
        }
        else {
            notifyItemRemoved(0)
        }
        notificationRemove()
    }

    private fun notificationRemove(){
        val size = listHistory.size
    }
}