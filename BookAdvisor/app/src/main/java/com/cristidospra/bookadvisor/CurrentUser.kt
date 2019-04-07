package com.cristidospra.bookadvisor

import com.cristidospra.bookadvisor.Models.Genre
import com.cristidospra.bookadvisor.Models.ReadingList
import com.cristidospra.bookadvisor.Models.User

class CurrentUser private constructor() : User() {

    var authToken: String = "d5758876839e656636eb7d43d2e651658402b982"

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
