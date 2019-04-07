package com.cristidospra.bookadvisor.Networking

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AuthToken(

    @SerializedName("token")
    var token: String = ""

) : Serializable {
}