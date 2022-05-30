package com.example.tempo.utils

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.*

class CaptureDateCurrent {

    private val local = Locale("pt", "BR")
    private val dateString = SimpleDateFormat("dd/MM/yyyy", local)
    private val dateDay = SimpleDateFormat("EEE, d MMM yyyy", local)
    private val hora = SimpleDateFormat("HH:mm", local)
    private val horaSecond = SimpleDateFormat("HH:mm:ss", local)

    fun captureDateCurrent(): String {
        val calendar = Calendar.getInstance().time
        return dateString.format(calendar)
    }

    fun captureDateDay(): String {
        val calendar = Calendar.getInstance().time
        return dateDay.format(calendar)
    }

    fun captureHoraCurrent(): String {
        val calendar = Calendar.getInstance().time
        return hora.format(calendar)
    }

    fun captureHoraCurrentSecond(): String {
        val calendar = Calendar.getInstance().time
        return horaSecond.format(calendar)
    }

    fun captureNextDate(date: String): String {
        val calendar = Calendar.getInstance()
        val data = this.dateString.parse(date)
        calendar.time = data
        calendar.add(Calendar.DAY_OF_MONTH, +1)
        return this.dateString.format(calendar.time)
    }
}