package com.cristidospra.bookadvisor.Models

import com.cristidospra.bookadvisor.Networking.ApiClient.Companion.BASE_URL
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class User(

    @SerializedName("firebaseUID")
    var firebasUID: String = "",

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

    fun nrOfBooks() : Int {

        return nrOfReadBooks() + nrOfWantToReadBooks()
    }

    fun nrOfReadBooks() : Int {

        return readingLists[ReadingList.ALREADY_READ].nrOfBooks()
    }

    fun nrOfWantToReadBooks() : Int {

        return readingLists[ReadingList.WANT_TO_READ].nrOfBooks()
    }

    fun getReadBooks(): ArrayList<Book> {

        return readingLists[ReadingList.ALREADY_READ].books
    }

    fun getWantToReadBooks() : ArrayList<Book> {

        return readingLists[ReadingList.WANT_TO_READ].books
    }

    fun nrFollowers() : Int {
        return followers.count()
    }

    fun nrFollowing() : Int {
        return following.count()
    }

    fun isFollowing(user: User) : Boolean {

        return following.map{it.email}.contains(user.email)
    }

    fun profilePic() : String {

        return BASE_URL + this.profilePicURL
    }

    fun addGenreToFavourites(genre: Genre) {

        if (!this.favouriteGenres.map { it.id }.contains(genre.id)) {
            this.favouriteGenres.add(genre)
        }
    }
}