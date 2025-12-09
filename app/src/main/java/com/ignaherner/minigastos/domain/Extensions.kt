package com.ignaherner.minigastos.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Double.format2(): String {
    return String.format(Locale.getDefault(), "%.2f", this)
}

fun Long.toDateText(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return format.format(date)
}