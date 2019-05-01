package com.cristidospra.bookadvisor.Models

import java.io.Serializable
import java.util.*

class Message(

    var sender: User = User(),

    var sentDate: Date = Date(),

    var content: String = "",

    var belongsToCurrentUser: Boolean = true

) : Serializable {
}