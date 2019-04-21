package com.cristidospra.bookadvisor.Models

import com.cristidospra.bookadvisor.Utils.Utils
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class Review(

    @SerializedName("user_review")
    @Transient
    var user: User = User(),

    @SerializedName("score")
    var givenRating: Float = 0.0f,

    @SerializedName("date")
    @Transient
    var dateString: String = String(),

    @SerializedName("content")
    var text: String = ""

) : Serializable {

    fun date() : Date {

        return  Utils.stringToDate(dateString)
    }
}