package com.example.tempo.utils

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class CaptureDateCurrent {

    private val local = Locale("pt", "BR")
    private val calendar = Calendar.getInstance().time
    private val dateString = SimpleDateFormat("dd/MM/yyyy", local)
    private val dateDay = SimpleDateFormat("EEE, d MMM yyyy", local)
    private val hora = SimpleDateFormat("HH:mm", local)
    private val horaSecond = SimpleDateFormat("HH:mm:ss", local)

    fun getDayOfWeek(data: String): String {
        val parser: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
        val day: DayOfWeek = DayOfWeek.from(parser.parse(data))
        return day.getDisplayName(TextStyle.FULL, local)
    }

    fun captureDateCurrent(): String {
        return dateString.format(calendar)
    }

    fun captureDateDay(): String {
        return dateDay.format(calendar)
    }

    fun captureHoraCurrent(): String {
        return hora.format(calendar)
    }

    fun captureHoraCurrentSecond(): String {
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