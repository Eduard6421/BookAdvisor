package com.cristidospra.bookadvisor.Utils

import androidx.core.content.ContextCompat
import com.cristidospra.bookadvisor.MyApplication
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private const val DATE_FORMAT = "DD.MM.YYYY"
    private val dateFormatter = SimpleDateFormat(DATE_FORMAT)

    fun stringToDate(date: String) : Date {

        return dateFormatter.parse(date)
    }

    fun formatDate(date: Date) : String {

        return dateFormatter.format(date)
    }

    fun getColor(resourceID: Int): Int {

        return ContextCompat.getColor(MyApplication.appContext!!, resourceID)
    }
}