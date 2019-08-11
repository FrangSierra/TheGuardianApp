package com.monzo.androidtest.utils

import java.text.DateFormat
import java.util.*

object DateUtils {

    @JvmStatic
    @JvmOverloads
    fun formatDate(date: Date, dateStyle: Int = DateFormat.SHORT): String {
        return DateFormat.getDateInstance(dateStyle).format(date)
    }
}