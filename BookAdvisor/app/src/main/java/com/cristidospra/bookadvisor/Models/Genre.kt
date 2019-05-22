package com.cristidospra.bookadvisor.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Genre(

    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    var name: String = ""

) : Serializable {
}