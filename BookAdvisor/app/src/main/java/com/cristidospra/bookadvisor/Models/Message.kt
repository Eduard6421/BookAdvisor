package com.cristidospra.bookadvisor.Models

import java.io.Serializable
import java.util.*

class Message(

    var sentDate: Date = Date(),

    var content: String = ""

) : Serializable {
}