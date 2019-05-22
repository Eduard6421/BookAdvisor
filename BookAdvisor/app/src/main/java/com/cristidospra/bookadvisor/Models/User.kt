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

        if (readingLists.isNotEmpty()) {
            return readingLists[ReadingList.ALREADY_READ].nrOfBooks()
        }
        return 0
    }

    fun nrOfWantToReadBooks() : Int {

        if (readingLists.isNotEmpty()) {
            return readingLists[ReadingList.WANT_TO_READ].nrOfBooks()
        }
        return 0
    }

    fun getReadBooks(): ArrayList<Book> {

        if (readingLists.isNotEmpty()) {
            return readingLists[ReadingList.ALREADY_READ].books
        }
        return ArrayList()
    }

    fun getWantToReadBooks() : ArrayList<Book> {

        if (readingLists.isNotEmpty()) {
            return readingLists[ReadingList.WANT_TO_READ].books
        }

        return ArrayList()
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

    fun updateReadingList(rl: ReadingList) {

        this.readingLists.removeAll { r -> r.title == rl.title }
        this.readingLists.add(rl)
    }
}