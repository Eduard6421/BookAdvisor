package com.cristidospra.bookadvisor.Networking

import com.cristidospra.bookadvisor.CurrentUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LoginApiManager {

     fun login(email: String, password: String, onSuccess: (AuthToken) -> Unit) {

        //Send request to server to get vacations for the current month
        val loginApiInterface: LoginApiInterface = ApiClient.getClient()?.create(LoginApiInterface::class.java)!!

        val call: Call<AuthToken> = loginApiInterface.getAuthToken(email, password)

        call.enqueue(object : Callback<AuthToken> {

            override fun onFailure(call: Call<AuthToken>, t: Throwable) {

                print("")
            }

            override fun onResponse(call: Call<AuthToken>, response: Response<AuthToken>) {

                onSuccess(response.body()!!)
            }

        })
    }

     fun register(email: String, password: String, onSuccess: (AuthToken) -> Unit) {

        //Send request to server to get vacations for the current month
        val loginApiInterface: LoginApiInterface = ApiClient.getClient()?.create(LoginApiInterface::class.java)!!

        val call: Call<AuthToken> = loginApiInterface.register(email, password)

        call.enqueue(object : Callback<AuthToken> {

            override fun onFailure(call: Call<AuthToken>, t: Throwable) {
                print("")
            }

            override fun onResponse(call: Call<AuthToken>, response: Response<AuthToken>) {

                onSuccess(response.body()!!)
            }

        })
    }

     fun logOut() {

        val loginApiInterface: LoginApiInterface = ApiClient.getClient()?.create(LoginApiInterface::class.java)!!

        val call: Call<Unit> = loginApiInterface.logout()

        call.enqueue(object : Callback<Unit> {

            override fun onFailure(call: Call<Unit>, t: Throwable) {
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                print("")
            }

        })

    }


}