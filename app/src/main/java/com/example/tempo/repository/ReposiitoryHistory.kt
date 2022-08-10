package com.example.tempo.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.example.tempo.constants.ConstantsHistory
import com.example.tempo.dataclass.CityData
import com.example.tempo.dbhistory.DataBaseHistory

class RepositoryHistory(dataBaseHistory: DataBaseHistory) {

    private val dbWrite = dataBaseHistory.writableDatabase
    private val dbRead = dataBaseHistory.readableDatabase
    private val tableName = ConstantsHistory.HISTORY.TABLE_NAME
    private lateinit var cursor: Cursor

    fun saveCity(history: CityData) {

        try {
            val insertValues = ContentValues()
            insertValues.run {
                put(ConstantsHistory.HISTORY.COLUMNS.IDCITY, history.idcity)
                put(ConstantsHistory.HISTORY.COLUMNS.CITY, history.city)
                put(ConstantsHistory.HISTORY.COLUMNS.STATE, history.state)
                put(ConstantsHistory.HISTORY.COLUMNS.LAT, history.latitude)
                put(ConstantsHistory.HISTORY.COLUMNS.LON, history.longitude)
            }
            dbWrite.insert(tableName, null, insertValues)

        } catch (e: Exception) { }
    }

    @SuppressLint("Range")
    fun historyList(): ArrayList<CityData> {

        val list: ArrayList<CityData> = ArrayList()
        try {
            val projection = arrayOf(
                    ConstantsHistory.HISTORY.COLUMNS.ID,
                    ConstantsHistory.HISTORY.COLUMNS.IDCITY,
                    ConstantsHistory.HISTORY.COLUMNS.CITY,
                    ConstantsHistory.HISTORY.COLUMNS.STATE,
                    ConstantsHistory.HISTORY.COLUMNS.LAT,
                    ConstantsHistory.HISTORY.COLUMNS.LON)

            val orderBy = ConstantsHistory.HISTORY.COLUMNS.ID

            cursor = dbRead.query (tableName, projection, null, null,
                    null, null, orderBy)

            if (cursor.count > 0) {
                while (cursor.moveToNext()) {

                    val id = cursor.getInt(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.ID))
                    val idCity = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.IDCITY))
                    val city = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.CITY))
                    val state = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.STATE))
                    val latitude = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.LAT))
                    val longitude = cursor.getString(
                            cursor.getColumnIndex(ConstantsHistory.HISTORY.COLUMNS.LON))

                    list.add(CityData(id, idCity, city, state, latitude, longitude))
                }
            }
            cursor.close()
            return list

        } catch (e: Exception) {
            return list
        }
    }

    fun removeHistory(id: Int) {

        try {
            val selection = ConstantsHistory.HISTORY.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            dbWrite.delete(tableName, selection, args)

        } catch (e: Exception) {  }
    }

    fun getCityExist(): Boolean {

       return try {
            val projection = arrayOf(ConstantsHistory.HISTORY.COLUMNS.CITY)
            val orderBy = ConstantsHistory.HISTORY.COLUMNS.CITY

            cursor = dbRead.query (tableName, projection, null, null,
                null, null, orderBy)

            cursor.count > 0

        } catch (e: Exception) { false }
    }

    fun removeAll(id: Int) {

        try {
            val selection = ConstantsHistory.HISTORY.COLUMNS.ID + " = ?"
            val args = arrayOf(id.toString())

            dbWrite.delete(tableName, selection, args)

        } catch (e: Exception) {  }
    }

    fun consultSizeList(): Int {
        cursor = dbRead.query (tableName, null, null, null,
                null, null, null)

        return cursor.count
    }
}

