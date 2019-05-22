package com.cristidospra.bookadvisor.Models

import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.Networking.ApiClient
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
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
    var authors: ArrayList<Author> = ArrayList(),

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
            toPrint = ("$toPrint${author.name}, ")
        }
        toPrint.removeRange(toPrint.length - 2, toPrint.length)

        return toPrint
    }

    fun nrRates() : Int {
        return reviews.count()
    }

    fun releaseDate() : Date {

        val DATE_FORMAT = "yyyy-MM-dd"
        val dateFormatter = SimpleDateFormat(DATE_FORMAT)

        val deletePosition = this.releaseDateString.indexOf('T')

        var cleanDate = this.releaseDateString
        if (deletePosition >= 0) {
            cleanDate = this.releaseDateString.removeRange(deletePosition, this.releaseDateString.length)
        }

        return dateFormatter.parse(cleanDate)
    }

    fun releaseDateString() : String {

        val DATE_FORMAT = "dd.MM.yyyy"
        val dateFormatter = SimpleDateFormat(DATE_FORMAT)

        val deletePosition = this.releaseDateString.indexOf('T')

        var cleanDate = this.releaseDateString
        if (deletePosition >= 0) {
            cleanDate = this.releaseDateString.removeRange(deletePosition, this.releaseDateString.length)
        }

        return dateFormatter.format(releaseDate())
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

    fun coverURL() : String {

        return /*ApiClient.BASE_URL + */this.coverURL
    }
}