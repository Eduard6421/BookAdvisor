package com.cristidospra.bookadvisor.Networking

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LoginApiManager {

     fun login(email: String, password: String, onSuccess: (AuthToken) -> Unit, onFailure: () -> Unit) {

        //Send request to server to get vacations for the current month
        val loginApiInterface: LoginApiInterface = ApiClient.getClient()?.create(LoginApiInterface::class.java)!!

        val call: Call<AuthToken> = loginApiInterface.getAuthToken(email, password)

        call.enqueue(object : Callback<AuthToken> {

            override fun onFailure(call: Call<AuthToken>, t: Throwable) {

                t.printStackTrace()
            }

            override fun onResponse(call: Call<AuthToken>, response: Response<AuthToken>) {

                if (response.body() != null) {
                    onSuccess(response.body()!!)
                }
                else if (response.code() != 200) {

                    onFailure()
                }
            }

        })
    }

     fun register(email: String, password: String, firebaseUID: String, firstName: String, lastName: String, onSuccess: (AuthToken) -> Unit) {

        //Send request to server to get vacations for the current month
        val loginApiInterface: LoginApiInterface = ApiClient.getClient()?.create(LoginApiInterface::class.java)!!

        val call: Call<AuthToken> = loginApiInterface.register(email, password, firebaseUID, firstName, lastName)

        call.enqueue(object : Callback<AuthToken> {

            override fun onFailure(call: Call<AuthToken>, t: Throwable) {

                t.printStackTrace()
            }

            override fun onResponse(call: Call<AuthToken>, response: Response<AuthToken>) {

                response.body()?.let { onSuccess(it) }
            }

        })
    }

     fun logOut() {

        val loginApiInterface: LoginApiInterface = ApiClient.getClient()?.create(LoginApiInterface::class.java)!!

        val call: Call<Unit> = loginApiInterface.logout()

        call.enqueue(object : Callback<Unit> {

            override fun onFailure(call: Call<Unit>, t: Throwable) {

                t.printStackTrace()
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                print("")
            }

        })

    }


}