package com.cristidospra.bookadvisor.Models

import com.cristidospra.bookadvisor.Utils.Utils
import java.util.*

class Review(

    var user: User = User(),

    var givenRating: Float = 0.0f,

    var date: Date = Date(),

    var text: String = ""
) {

    fun dateAsString() : String {

        return  Utils.formatDate(date)
    }
}