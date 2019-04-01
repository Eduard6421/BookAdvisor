package com.cristidospra.bookadvisor.Models

import com.cristidospra.bookadvisor.Utils.Utils
import java.util.*

class Book(

    var coverURL: String = "",

    var title: String = "",

    var author: String = "",

    var nrPages: Int = 0,

    var releaseDate: Date = Date(),

    var prologue: String = "",

    var rating: Float = 0.0f,

    var nrRates: Int = 0,

    var readStatus: ReadStatus = ReadStatus.NOTHING,

    var genres: ArrayList<Genre> = ArrayList()
) {

    fun releaseDateAsString(): String {

        return Utils.formatDate(releaseDate)
    }
}