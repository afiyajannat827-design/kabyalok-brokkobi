package com.example.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toBengaliNumerals(): String {
    val englishDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    val bengaliDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
    var result = this
    for (i in 0..9) {
        result = result.replace(englishDigits[i], bengaliDigits[i])
    }
    return result
}

fun Int.toBengaliNumerals(): String = this.toString().toBengaliNumerals()
fun Long.toBengaliNumerals(): String = this.toString().toBengaliNumerals()

fun formatBengaliDate(timestamp: Long): String {
    val date = Date(timestamp)
    val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale("bn", "BD"))
    val formatted = try {
        sdf.format(date)
    } catch (e: Exception) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
    }
    return formatted.toBengaliNumerals()
}

fun formatBengaliDateTime(timestamp: Long): String {
    val date = Date(timestamp)
    val sdf = SimpleDateFormat("dd MMM, yyyy • hh:mm a", Locale("bn", "BD"))
    val formatted = try {
        sdf.format(date)
    } catch (e: Exception) {
        SimpleDateFormat("dd/MM/yyyy • hh:mm", Locale.getDefault()).format(date)
    }
    return formatted.toBengaliNumerals()
}
