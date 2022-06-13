package com.example.tempo.interfaces

import com.example.tempo.dataclass.CityData

interface OnItemClickRecycler {
    fun clickCity(id: String, city: String, state: String)
}
interface OnItemClickItemCity {
    fun clickItemCity(item: CityData)
}
interface OnItemClickDeleteCity {
    fun clickDeleteCity(item: CityData, position: Int)
}