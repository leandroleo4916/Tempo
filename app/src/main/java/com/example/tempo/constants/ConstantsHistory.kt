package com.example.tempo.constants

class ConstantsHistory private constructor(){

    object HISTORY {
        const val TABLE_NAME = "history"
        object COLUMNS {
            const val ID = "id"
            const val IDCITY = "idcity"
            const val CITY = "city"
            const val STATE = "state"
        }
    }
}