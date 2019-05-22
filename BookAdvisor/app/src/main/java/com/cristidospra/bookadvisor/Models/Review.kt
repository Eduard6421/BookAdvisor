package com.cristidospra.bookadvisor.Models

import com.cristidospra.bookadvisor.Utils.Utils
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Review(

    @SerializedName("user_review")
    var user: User = User(),

    @SerializedName("score")
    var givenRating: Float = 0.0f,

    @SerializedName("date")
    var dateString: String = String(),

    @SerializedName("content")
    var text: String = ""

) : Serializable {

    fun date() : Date {

        val DATE_FORMAT = "yyyy-MM-dd"
        val dateFormatter = SimpleDateFormat(DATE_FORMAT)

        val deletePosition = this.dateString.indexOf('T')

        var cleanDate = this.dateString
        if (deletePosition >= 0) {
            cleanDate = this.dateString.removeRange(deletePosition, this.dateString.length)
        }

        return dateFormatter.parse(cleanDate)
    }

    fun timeStamp() : String {

        val format = SimpleDateFormat("dd.MM.yyyy")

        return format.format(date())
    }
}