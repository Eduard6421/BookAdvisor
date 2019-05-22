package com.cristidospra.bookadvisor.Utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.MyApplication
import com.cristidospra.bookadvisor.R
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    private const val DATE_FORMAT = "dd.MM.yyyy HH:mm"
    private val dateFormatter = SimpleDateFormat(DATE_FORMAT)

    const val SHARED_PREF = "PREFERENCES"
    const val SHARED_PREF_EMAIL = "EmailKey"
    const val SHARED_PREF_PASSWORD = "PasswordKey"

    fun stringToDate(date: String) : Date {

        return dateFormatter.parse(date)
    }

    fun formatDate(date: Date) : String {

        return dateFormatter.format(date)
    }

    fun getColor(resourceID: Int): Int {

        return ContextCompat.getColor(MyApplication.appContext!!, resourceID)
    }

    fun loadBookImage(currentContext: Context, imageView: ImageView, source: String) {

        val glideUrl = GlideUrl(
            source,
            LazyHeaders.Builder()
                .addHeader("Authorization", "Token ${CurrentUser.instance.authToken}")
                .build()
        )

        Glide.with(currentContext).load(glideUrl)
            .apply(RequestOptions().placeholder(R.drawable.cover_placeholder))
            .apply(RequestOptions().transforms(CenterInside()))
            .into(imageView)
    }

    fun loadPersonImage(currentContext: Context, imageView: ImageView, source: String) {

        val glideUrl = GlideUrl(
            source,
            LazyHeaders.Builder()
                .addHeader("Authorization", "Token ${CurrentUser.instance.authToken}")
                .build()
        )

        Glide.with(currentContext).load(glideUrl)
            .apply(RequestOptions().placeholder(R.drawable.ic_default_profile))
            .apply(RequestOptions().transforms(CircleCrop(), CenterInside()))
            .into(imageView)
    }

    fun closeKeyboard(context: Context, view: View) {

        val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}