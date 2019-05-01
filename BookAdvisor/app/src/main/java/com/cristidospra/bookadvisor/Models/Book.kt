package com.cristidospra.bookadvisor.Models

import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.Utils.Utils
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class Book(

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("cover")
    var coverURL: String = "",

    @SerializedName("title")
    var title: String = "",

    @SerializedName("authors")
    var authors: ArrayList<String> = ArrayList(),

    @SerializedName("no_pages")
    var nrPages: Int = 0,

    @SerializedName("publish_date")
    var releaseDateString: String = "",

    @SerializedName("description")
    var prologue: String = "",

    @SerializedName("rating")
    var rating: Float = 0.0f,

    @Transient
    var readStatus: ReadStatus = ReadStatus.NOTHING,

    @SerializedName("books_tags")
    var genres: ArrayList<Genre> = ArrayList(),

    @SerializedName("reviews")
    var reviews: ArrayList<Review> = ArrayList()

) : Serializable {


    fun authorsToString() : String {

        var toPrint = ""

        for (author in this.authors) {
            toPrint = (toPrint + author)
        }

        return toPrint
    }

    fun nrRates() : Int {
        return reviews.count()
    }

    fun releaseDate() : Date {
        return Utils.stringToDate(releaseDateString)
    }

    fun getContainedByReadingLists() : ArrayList<ReadingList> {

        val readingLists: ArrayList<ReadingList> = ArrayList()

        for (readingList in CurrentUser.instance.readingLists) {

            if (readingList.contains(this)) {
                readingLists.add(readingList)
            }
        }

        return readingLists
    }
}