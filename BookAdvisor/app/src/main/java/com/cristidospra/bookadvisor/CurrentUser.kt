package com.cristidospra.bookadvisor

import com.cristidospra.bookadvisor.Models.User

class CurrentUser private constructor() : User() {

    var authToken: String = ""

    // Singleton design pattern
    companion object {
        var instance: CurrentUser = CurrentUser()
    }

     fun fromUser(user: User) {

        instance.email = user.email
        instance.firebasUID = user.firebasUID
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
