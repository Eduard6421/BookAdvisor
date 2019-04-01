package com.cristidospra.bookadvisor.Utils

import androidx.core.content.ContextCompat
import com.cristidospra.bookadvisor.MyApplication
import java.util.*

object Utils {

    private const val DATE_FORMAT = "DD.MM.YYYY"

    //private val dateFormatter

    fun formatDate(date: Date) : String {

        return ""
    }

    fun getColor(resourceID: Int): Int {

        return ContextCompat.getColor(MyApplication.appContext!!, resourceID)
    }
}