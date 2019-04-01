package com.cristidospra.bookadvisor.Models

open class User(

    var profilePicURL: String = "",

    var firstName: String = "",

    var lastName: String = "",

    var readingLists: ArrayList<ReadingList> = ArrayList(),

    var nrFollowers: Int = 0,

    var nrFollowing: Int = 0,

    var isFollowed: Boolean = false,

    var favouriteGenres: ArrayList<Genre> = ArrayList()
) {

    fun fullName() : String {

        return "$firstName  $lastName"
    }
}