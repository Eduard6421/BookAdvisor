package com.cristidospra.bookadvisor.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Conversation(

    var chatUID: String = "",

    @SerializedName("user")
    var user: User = User(),

    @SerializedName("last_message")
    var lastMessage: Message? = null

) : Serializable {
}