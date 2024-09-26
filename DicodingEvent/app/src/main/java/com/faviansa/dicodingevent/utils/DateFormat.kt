package com.faviansa.dicodingevent.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormat {
    fun formatDate(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        val date = inputFormat.parse(input)
        return date?.let { outputFormat.format(it) } ?: ""
    }
}