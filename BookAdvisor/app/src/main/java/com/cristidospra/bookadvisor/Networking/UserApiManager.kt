package com.cristidospra.bookadvisor.Networking

import android.graphics.Bitmap
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Models.Conversation
import com.cristidospra.bookadvisor.Models.ReadingList
import com.cristidospra.bookadvisor.Models.User
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

object UserApiManager {

     fun getUser(email: String, onSuccess: (User) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<User> = userApiInterface.getUser(email)

        call.enqueue(object : Callback<User> {

            override fun onFailure(call: Call<User>, t: Throwable) {
                print("")
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                response.body()?.let { onSuccess(it) }
            }

        })
     }

    fun getUserByFirebase(firebaseUID: String, onSuccess: (User) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<User> = userApiInterface.getUserByFirebaseUID(firebaseUID)

        call.enqueue(object : Callback<User> {

            override fun onFailure(call: Call<User>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                response.body()?.let { onSuccess(it) }
            }

        })
    }

    fun getUserByName(name: String, onSuccess: (ArrayList<User>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<User>> = userApiInterface.getUserByName(name)

        call.enqueue(object : Callback<ArrayList<User>> {

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                print("")
            }

            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                print("")
                response.body()?.let { onSuccess(it) }
            }

        })

    }


     fun getRecommendedBooks(onSuccess: (ArrayList<Book>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<Book>> = userApiInterface.getRecommended()

        call.enqueue(object : Callback<ArrayList<Book>> {

            override fun onFailure(call: Call<ArrayList<Book>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ArrayList<Book>>, response: Response<ArrayList<Book>>) {

                print("")
                response.body()?.let { onSuccess(it) }
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

    fun changeProfilePic(photo: Bitmap, onSuccess: (User) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val stream = ByteArrayOutputStream()

        photo.compress(Bitmap.CompressFormat.PNG, 100, stream)

        val cover: RequestBody = RequestBody.create(MediaType.parse("image/*"), stream.toByteArray())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("profile", "Profile_pic", cover)

        val call: Call<User> = userApiInterface.changeProfilePic(body)

        call.enqueue(object : Callback<User> {

            // Failed request
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                t?.printStackTrace()
            }

            // Successful request
            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                response?.body()?.let { onSuccess(it) }
            }

        })
    }

     fun getFollowers(onSuccess: (ArrayList<User>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<User>> = userApiInterface.getFollowers()

        call.enqueue(object : Callback<ArrayList<User>> {

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                print("")
                response.body()?.let { onSuccess(it) }
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
                response.body()?.let { onSuccess(it) }
            }

        })

     }

    fun getRecommendedPeople(onSuccess: (ArrayList<User>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<User>> = userApiInterface.getRecommendedPeople()

        call.enqueue(object : Callback<ArrayList<User>> {

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                print("")
            }

            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                print("")
                response.body()?.let { onSuccess(it) }
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

     fun updateReadingList(readingList: ReadingList, onSuccess: (ReadingList) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ReadingList> = userApiInterface.updateReadingList(readingList.title, readingList)

        call.enqueue(object : Callback<ReadingList> {

            // Failed request
            override fun onFailure(call: Call<ReadingList>?, t: Throwable?) {
                print("")
            }

            // Successful request
            override fun onResponse(call: Call<ReadingList>?, response: Response<ReadingList>?) {
                print("")
                response?.body()?.let { onSuccess(it) }
            }

        })
    }

     fun getUsers(onSuccess: (ArrayList<User>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<User>> = userApiInterface.getUsers()

        call.enqueue(object : Callback<ArrayList<User>> {

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ArrayList<User>>, response: Response<ArrayList<User>>) {
                print("")
                response.body()?.let { onSuccess(it) }
            }

        })

    }

     fun getConversations(onSuccess: (ArrayList<Conversation>) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<ArrayList<Conversation>> = userApiInterface.getConversations()

        call.enqueue(object : Callback<ArrayList<Conversation>> {

            override fun onFailure(call: Call<ArrayList<Conversation>>, t: Throwable) {

            }

            override fun onResponse(call: Call<ArrayList<Conversation>>, response: Response<ArrayList<Conversation>>) {
                print("")
                response.body()?.let { onSuccess(it) }
            }
        })

    }

     fun getConversation(user: User, onSuccess: (Conversation) -> Unit) {

        val userApiInterface: UserApiInterface = ApiClient.getClient()?.create(UserApiInterface::class.java)!!

        val call: Call<Conversation> = userApiInterface.getConversation(user.email)

        call.enqueue(object : Callback<Conversation> {

            override fun onFailure(call: Call<Conversation>, t: Throwable) {

            }

            override fun onResponse(call: Call<Conversation>, response: Response<Conversation>) {
                print("")
                response.body()?.let { onSuccess(it) }
            }

        })
    }


}