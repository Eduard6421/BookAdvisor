package com.cristidospra.bookadvisor.Models

import com.google.gson.annotations.SerializedName
import java.util.HashMap

class FirebaseMessage(

                        @SerializedName("sender")
                        var senderUID: String = "",

                        @SerializedName("content")
                        var content: String = "",

                        @SerializedName("timestamp")
                        var timeStamp: String = ""
) {

    constructor(message: Message) : this(message.sender.firebasUID, message.content, message.timeStamp())
    constructor(hmap: HashMap<String, String>?) : this(hmap!!["senderUID"].toString(), hmap["content"].toString(), hmap["timeStamp"].toString())
}