package com.cristidospra.bookadvisor.Models

class ReadingList(

    var title: String = "",

    var genres: ArrayList<Genre> = ArrayList(),

    var books: ArrayList<Book> = ArrayList()
) {

    fun nrOfBooks(): Int {

        return books.count()
    }
}