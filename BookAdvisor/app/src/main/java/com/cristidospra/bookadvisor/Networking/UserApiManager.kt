package com.cristidospra.bookadvisor.Networking

import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.Models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserApiManager {

     fun getUser(email: String, onSucces: (User) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<User> = userApiInterface.getUser(email)

        call.enqueue(object : Callback<User> {

            override fun onFailure(call: Call<User>, t: Throwable) {
                print("")
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                print("")
                onSucces(response.body()!!)
            }

        })
    }

     fun getRecommendedBooks(onSuccess: (ArrayList<Recommendation>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<Recommendation>> = userApiInterface.getRecommended()

        call.enqueue(object : Callback<ArrayList<Recommendation>> {

            override fun onFailure(call: Call<ArrayList<Recommendation>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ArrayList<Recommendation>>, response: Response<ArrayList<Recommendation>>) {

                print("")
                onSuccess(response.body()!!)
            }

        })

    }

     fun updateUser(user: User) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<Unit> = userApiInterface.updateUser(user)

        call.enqueue(object : Callback<Unit> {

            // Failed request
            override fun onFailure(call: Call<Unit>?, t: Throwable?) {
                print("")
            }

            // Successful request
            override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {
                print("")
            }

        })
    }

     fun getFollowers(onSucces: (ArrayList<User>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<User>> = userApiInterface.getFollowers()

        call.enqueue(object : Callback<ArrayList<User>> {

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                print("")
                onSucces(response.body()!!)
            }

        })

    }

     fun getFollowing(onSuccess: (ArrayList<User>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<User>> = userApiInterface.getFollowing()

        call.enqueue(object : Callback<ArrayList<User>> {

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                print("")
            }

            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                print("")
                onSuccess(response.body()!!)
            }

        })

    }


     fun follow(userToFollow: User, onSuccess: () -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<Unit> = userApiInterface.follow(userToFollow.email)

        call.enqueue(object : Callback<Unit> {

            // Failed request
            override fun onFailure(call: Call<Unit>?, t: Throwable?) {
            }

            // Successful request
            override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {

                CurrentUser.instance.following.add(userToFollow)
                onSuccess()
            }
        })
    }

     fun follow(email: String) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<Unit> = userApiInterface.follow(email)

        call.enqueue(object : Callback<Unit> {

            // Failed request
            override fun onFailure(call: Call<Unit>?, t: Throwable?) {
                print("")
            }

            // Successful request
            override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {
                print("")
            }

        })
    }


     fun addReadingList(readingList: ReadingList) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<Unit> = userApiInterface.addReadingList(readingList)

        call.enqueue(object : Callback<Unit> {

            // Failed request
            override fun onFailure(call: Call<Unit>?, t: Throwable?) {
                print("")
            }

            // Successful request
            override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {
                print("")
            }

        })
    }

     fun updateReadingList(readingList: ReadingList) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<Unit> = userApiInterface.updateReadingList(readingList.title, readingList)

        call.enqueue(object : Callback<Unit> {

            // Failed request
            override fun onFailure(call: Call<Unit>?, t: Throwable?) {
                print("")
            }

            // Successful request
            override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {
                print("")
            }

        })
    }

     fun getUsers(onSucces: (ArrayList<User>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<User>> = userApiInterface.getUsers()

        call.enqueue(object : Callback<ArrayList<User>> {

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                print("")
                onSucces(response.body()!!)
            }

        })

    }

     fun getConversations(onSucces: (ArrayList<Conversation>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<Conversation>> = userApiInterface.getConversations()

        call.enqueue(object : Callback<ArrayList<Conversation>> {

            override fun onFailure(call: Call<ArrayList<Conversation>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ArrayList<Conversation>>, response: Response<ArrayList<Conversation>>) {
                print("")
                onSucces(response.body()!!)
            }
        })

    }

     fun getConversation(user: User, onSucces: (Conversation) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<Conversation> = userApiInterface.getConversation(user.email)

        call.enqueue(object : Callback<Conversation> {

            override fun onFailure(call: Call<Conversation>, t: Throwable) {

            }

            override fun onResponse(call: Call<Conversation>, response: Response<Conversation>) {
                print("")
                onSucces(response.body()!!)
            }

        })
    }


}