package com.cristidospra.bookadvisor.Networking

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApiInterface {

    @FormUrlEncoded
    @POST("login")
    fun getAuthToken(@Field("email") email: String,
                     @Field("password") password: String,
                     @Header("Authorization") token: String = "")
            : Call<AuthToken>


    @FormUrlEncoded
    @POST("logout")
    fun logout(): Call<Unit>


    @FormUrlEncoded
    @POST("register")
    fun register(@Field("email") email: String,
                 @Field("password") password: String,
                 @Field("firebaseUID") firebaseUID: String,
                 @Field("first_name") firstName: String,
                 @Field("last_name") lastName: String,
                 @Header("Authorization") token: String = "")
        : Call<AuthToken>


}