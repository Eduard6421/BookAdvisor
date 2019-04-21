package com.cristidospra.bookadvisor.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ReadingList(

    @SerializedName("title")
    var title: String = "",

    @SerializedName("tags")
    var genres: ArrayList<Genre> = ArrayList(),

    @SerializedName("books")
    var books: ArrayList<Book> = ArrayList()

) : Serializable {

    companion object {

        const val ALREADY_READ = 0
        const val WANT_TO_READ = 1
    }

    fun nrOfBooks(): Int {

        return books.count()
    }
}