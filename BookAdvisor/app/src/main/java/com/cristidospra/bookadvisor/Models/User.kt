package com.cristidospra.bookadvisor.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class User(

    @SerializedName("email")
    var email: String = "",

    @SerializedName("profile_picture")
    var profilePicURL: String = "",

    @SerializedName("first_name")
    var firstName: String = "",

    @SerializedName("last_name")
    var lastName: String = "",

    @SerializedName("reading_lists")
    var readingLists: ArrayList<ReadingList> = ArrayList(),

    @SerializedName("followers")
    var followers: ArrayList<User> = ArrayList(),

    @SerializedName("following")
    var following: ArrayList<User> = ArrayList(),

    @Transient
    var isFollowed: Boolean = false,

    @SerializedName("favorite_tags")
    var favouriteGenres: ArrayList<Genre> = ArrayList()

) : Serializable {

    fun fullName() : String {

        return "$firstName  $lastName"
    }

    fun nrFollowers() : Int {
        return followers.count()
    }

    fun nrFollowing() : Int {
        return following.count()
    }
}