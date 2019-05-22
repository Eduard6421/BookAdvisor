package com.cristidospra.bookadvisor.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Message(

    @Transient
    var sender: User = User(),

    @Transient
    var receiver: User = User(),

    /*@SerializedName("senderUID")
    var senderFirebaseUID: String = "",*/

    @SerializedName("sent_date")
    @Transient
    var sentDate: Date = Date(),

    @SerializedName("content")
    var content: String = ""

) : Serializable {

    fun timeStamp() : String {

        val format = SimpleDateFormat("dd.MM.yyyy HH:mm")

        return format.format(this.sentDate)
    }

    fun accurateTimeStamp() : String {

        val format = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")

        return format.format(this.sentDate)
    }
}