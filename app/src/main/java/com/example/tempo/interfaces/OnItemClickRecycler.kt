package com.example.tempo.interfaces

interface OnItemClickRecycler {
    fun clickCity(id: String, city: String, state: String)
}

interface ItemCityLocation {
    fun cityLocation(id: String, city: String, state: String)
}