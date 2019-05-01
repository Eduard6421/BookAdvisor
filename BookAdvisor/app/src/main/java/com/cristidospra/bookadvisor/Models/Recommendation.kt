package com.cristidospra.bookadvisor.Models

import java.io.Serializable

class Recommendation(

    var title: String,

    var books: ArrayList<Book>

) : Serializable {
}