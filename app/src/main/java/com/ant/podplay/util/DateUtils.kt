package com.ant.podplay.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun jsonDateToShortDate(jsonDate: String?): String {

        // If the data is null, return "-", avoiding a call to Android Resources
        if (jsonDate == null) {
            return "-"
        }

        // Define a SimpleDateFormat
        val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        // Parse jsonDate to a Date object. If it gets a null value, then return "-"
        val date = inFormat.parse(jsonDate) ?: return "-"

        // Format the date and return it
        val outputFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
        return outputFormat.format(date)
    }
}
