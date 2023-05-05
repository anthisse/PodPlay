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

    // Convert an xml date to a SimpleDateFormat
    fun xmlDateToDate(dateString: String?): Date {
        val date = dateString ?: return Date()
        val inFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault())
        return inFormat.parse(date) ?: Date()
    }

    // Convert a date to a ShortDate
    fun dateToShortDate(date: Date): String {
        val outputFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
        return outputFormat.format(date)
    }
}
