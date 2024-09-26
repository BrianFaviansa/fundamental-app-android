package com.faviansa.dicodingevent.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormat {
    fun formatCardDate(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        val date = inputFormat.parse(input)
        return date?.let { outputFormat.format(it) } ?: ""
    }

    fun formatDetailTime(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val time = inputFormat.parse(input)
        return time?.let { outputFormat.format(it) } ?: ""
    }
}