package com.cristidospra.bookadvisor.Networking

import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Models.Conversation
import com.cristidospra.bookadvisor.Models.ReadingList
import com.cristidospra.bookadvisor.Models.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface UserApiInterface {

    @GET("get-user/{email}")
    fun getUser(@Path("email") email: String) : Call<User>


    @GET("get-user-by-firebase/{id}")
    fun getUserByFirebaseUID(@Path("id") firebaseUID: String) : Call<User>


    @GET("find-user/{name}")
    fun getUserByName(@Path("name") name: String) : Call<ArrayList<User>>


    @GET("recommended-books/")
    fun getRecommended() : Call<ArrayList<Book>>


    @PUT("update-user/")
    fun updateUser(@Body user: User): Call<Unit>


    @GET("get-followers/")
    fun getFollowers() : Call<ArrayList<User>>


    @GET("get-following/")
    fun getFollowing() : Call<ArrayList<User>>


    @FormUrlEncoded
    @POST("follow")
    fun follow(@Field("email_to_follow") email: String) : Call<Unit>

    @GET("find-new-people")
    fun getRecommendedPeople() : Call<ArrayList<User>>


    @PUT("add-reading-list/")
    fun addReadingList(@Body readingList: ReadingList) : Call<Unit>


    @PUT("update-reading-list/{name}")
    fun updateReadingList(@Path("name") name: String,
                          @Body readingList: ReadingList) : Call<ReadingList>


    @Multipart
    @PUT("update-user-pic/")
    fun changeProfilePic(@Part image: MultipartBody.Part) : Call<User>

    @GET("users")
    fun getUsers() : Call<ArrayList<User>>


    @GET("get-conversations/")
    fun getConversations() : Call<ArrayList<Conversation>>


    @GET("get-conversation/{email_destinatar")
    fun getConversation(@Path("email_destinatar") email: String) : Call<Conversation>

    @FormUrlEncoded
    @POST("send-message")
    fun sendMessage(@Field("email_destinatar") email: String,
                    @Field("content") content: String,
                    @Field("date") date: Date) : Call<Unit>
}