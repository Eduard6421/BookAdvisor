package com.cristidospra.bookadvisor

import com.cristidospra.bookadvisor.Models.Genre
import com.cristidospra.bookadvisor.Models.ReadingList
import com.cristidospra.bookadvisor.Models.User

class CurrentUser private constructor() : User() {

    var authToken: String = "d16ba700967c4bc6422a6b73d21c97c3893ea521"

    // Singleton design pattern
    companion object {
        var instance: CurrentUser = CurrentUser()
    }

    private fun fromUser(user: User) {

        instance.email = user.email
        instance.profilePicURL = user.profilePicURL
        instance.firstName = user.firstName
        instance.lastName = user.lastName
        instance.readingLists = user.readingLists
        instance.followers = user.followers
        instance.following = user.following
        instance.isFollowed = user.isFollowed
        instance.favouriteGenres = user.favouriteGenres

    }
}
