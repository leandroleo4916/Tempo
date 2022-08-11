package com.example.tempo.activity.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tempo.dataclass.CityData
import com.example.tempo.repository.RepositoryHistory

class SearchViewModel(private val repository: RepositoryHistory) : ViewModel() {

    val listHistory = MutableLiveData<ArrayList<CityData>>()

    fun getHistory() { listHistory.value = repository.historyList() }

    fun saveHistory(city: CityData) {
        val resCity = repository.getCityExist(city.city)
        if (!resCity) repository.saveCity(city)
    }

    fun deleteHistory(id: Int) = repository.removeHistory(id)
}