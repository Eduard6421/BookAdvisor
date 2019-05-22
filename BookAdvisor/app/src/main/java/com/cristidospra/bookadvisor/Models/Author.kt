package com.cristidospra.bookadvisor.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Author(
                @SerializedName("id")
                var id: Int = -1,

                @SerializedName("name")
                var name: String = ""

) : Serializable {
}