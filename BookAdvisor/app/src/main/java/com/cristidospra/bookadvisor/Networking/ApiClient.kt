package com.cristidospra.bookadvisor.Networking

import com.cristidospra.bookadvisor.CurrentUser
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Setup http connection to server via Retrofit
 *
 */

class ApiClient {

    companion object {

        //Sets BEARER_TOKEN field of each http request with the given token of the current user
        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Token " + CurrentUser.instance.authToken)
                .build()
            chain.proceed(newRequest)
        }.build()


        //Server's base URL
        const val BASE_URL: String = "http://63.33.39.222:8000/"

        //Initialize retrofit instance
        private var retrofit: Retrofit? = null

        //Singleton design pattern
        fun getClient(): Retrofit? {

            if (retrofit == null) {

                retrofit = Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return retrofit
        }
    }
}