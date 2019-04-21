package com.cristidospra.bookadvisor.Networking

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginApiInterface {

    @FormUrlEncoded
    @POST("login")
    fun getAuthToken(@Field("email") email: String,
                     @Field("password") password: String)
            : Call<AuthToken>


    @FormUrlEncoded
    @POST("logout")
    fun logout(): Call<Unit>


    @FormUrlEncoded
    @POST("register")
    fun register(@Field("email") email: String,
                 @Field("password") password: String)
        : Call<AuthToken>


}