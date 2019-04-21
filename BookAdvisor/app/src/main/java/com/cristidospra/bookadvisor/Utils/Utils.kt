package com.cristidospra.bookadvisor.Utils

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.cristidospra.bookadvisor.MyApplication
import com.cristidospra.bookadvisor.R
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    private const val DATE_FORMAT = "DD.MM.YYYY"
    private val dateFormatter = SimpleDateFormat(DATE_FORMAT)

    val SHARED_PREF = "PREFERENCES"
    val SHARED_PREF_EMAIL = "EmailKey"
    val SHARED_PREF_PASSWORD = "PasswordKey"

    fun stringToDate(date: String) : Date {

        return dateFormatter.parse(date)
    }

    fun formatDate(date: Date) : String {

        return dateFormatter.format(date)
    }

    fun getColor(resourceID: Int): Int {

        return ContextCompat.getColor(MyApplication.appContext!!, resourceID)
    }

    fun loadImage(currentContext: Context, imageView: ImageView, source: String) {

        Glide.with(currentContext).load(source).placeholder(R.drawable.cover_placeholder).into(imageView)
    }
}