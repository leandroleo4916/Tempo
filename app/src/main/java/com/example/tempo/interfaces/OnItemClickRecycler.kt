package com.example.tempo.interfaces

import com.example.tempo.dataclass.CityData

interface OnItemClickRecycler {
    fun clickCity(item: CityData)
}
interface OnClickItemHistoryCity {
    fun clickItemHistoryCity(item: CityData)
}
interface OnItemClickDeleteCity {
    fun clickDeleteCity(item: CityData, position: Int)
}